import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String castle = "\uD83C\uDFF0";

        int sizeBoard = 5;
        int step = 0;

        Person person = new Person(sizeBoard);

        Random random = new Random();
        int castleX = 1 + random.nextInt(sizeBoard);
        int castleY = 1;

        String[][] board = new String[sizeBoard][sizeBoard];
        for (int y = 1; y <= sizeBoard; y++) {
            for (int x = 1; x <= sizeBoard; x++) {
                board[y - 1][x - 1] = "  ";
            }
        }

        int countMonster = sizeBoard * sizeBoard - sizeBoard - 1;
        MalenkiyMonstrik[] malenkiyMonstriks = new MalenkiyMonstrik[countMonster];
        int count = 0;

        while (count < countMonster) {
            MalenkiyMonstrik malenkiyMonstrik;

            if (random.nextBoolean()) {
                malenkiyMonstrik = new MalenkiyMonstrik(sizeBoard);
            } else {
                malenkiyMonstrik = new BolshoyMonstrik(sizeBoard);
            }

            int row = malenkiyMonstrik.getY() - 1;
            int column = malenkiyMonstrik.getX() - 1;

            if (board[row][column].equals("  ")) {
                board[row][column] = malenkiyMonstrik.getImage();
                malenkiyMonstriks[count] = malenkiyMonstrik;
                count++;
            }
        }

        board[castleY - 1][castleX - 1] = castle;

        System.out.println("Хай! Погнали играть? (Да или НЕТ)");

        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();

        System.out.println("Твой ответ:\t" + answer);

        switch (answer) {
            case "Да":
                System.out.println("Какая сложность игры (от 1 до 5):");
                int difficultGame = scanner.nextInt();
                System.out.println("Выбранная сложность:\t" + difficultGame);

                while (true) {
                    board[person.getY() - 1][person.getX() - 1] = person.getImage();
                    outputBoard(board, person.getLive());

                    System.out.println("Вводи, куда будешь ходить своим персонажем "
                            + "\nКоординаты твоего персонажа — (x: " + person.getX()
                            + ", y: " + person.getY() + ")");

                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    System.out.println(x + ", " + y);

                    if (person.isMoveCorrect(x, y)) {
                        if (board[y - 1][x - 1].equals("  ")) {
                            board[person.getY() - 1][person.getX() - 1] = "  ";
                            person.move(x, y);
                            step++;

                            System.out.println("Ход корректный; новые координаты: "
                                    + person.getX() + ", " + person.getY()
                                    + "\nХод номер: " + step);
                        } else if (board[y - 1][x - 1].equals(castle)) {
                            System.out.println("Красавчик. Ты прошёл эту игру!");
                            break;
                        } else {
                            for (MalenkiyMonstrik malenkiyMonstrik : malenkiyMonstriks) {
                                if (malenkiyMonstrik.conflictPerson(x, y)) {
                                    if (malenkiyMonstrik.taskMonster(difficultGame)) {
                                        board[person.getY() - 1][person.getX() - 1] = "  ";
                                        person.move(x, y);
                                        step++;
                                    } else {
                                        person.downLive();
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("Некорректный ход");
                    }

                    if (person.getLive() <= 0) {
                        break;
                    }
                }

                if (person.getLive() <= 0) {
                    System.out.println("Эх, жизни закончились. Итог: ...");
                }
                break;

            default:
                System.out.println("Данные введены некорректно");
                break;
        }
    }

    static void outputBoard(String[][] board, int live) {
        String leftBlock = "| ";
        String rightBlock = "|";
        String wall = "* —— * —— * —— * —— * —— *";

        for (String[] row : board) {
            System.out.println(wall);
            for (String col : row) {
                System.out.print(leftBlock + col + " ");
            }
            System.out.println(rightBlock);
        }
        System.out.println(wall);

        System.out.println("Количество жизней:\t" + live + "\n");
    }
}