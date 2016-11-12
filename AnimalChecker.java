import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by lixiangmin on 2016/10/24.
 */
public class AnimalChecker {
    static String[][] animal = new String[7][9];//  储存动物地图
    static String[][] map = new String[7][9];//  储存地形地图
    static String[][][] boardHistory = new String[10000][7][9];//  储存每次落子之后的地图
    static String[][][] boardBeforeUndo = new String[10000][7][9];//  用于储存悔棋之前的地图
    static int currentStep = 0;//  玩家落子的次数
    static int lastStep = 0;//  悔棋的次数
    static String game;//  用于判断胜负和重启游戏以及退出游戏
    static Scanner input = new Scanner(System.in);

    //  游戏的主要结构
    public static void main(String[] args) throws FileNotFoundException {
        loop:
        for (; ; ) {
            //  初始化游戏
            Scanner scanner = new Scanner(new File("tile.txt"));
            Scanner scanner1 = new Scanner(new File("animal.txt"));
            String[] map1 = {" 　 ", " 水 ", " 陷 ", " 家 ", " 陷 ", " 家 "};
            String[] animalLeft = {" 　 ", "1鼠 ", "2猫 ", "3狼 ", "4狗 ", "5豹 ", "6虎 ", "7狮 ", "8象 "};
            String[] animalRight = {" 　 ", " 鼠1", " 猫2", " 狼3", " 狗4", " 豹5", " 虎6", " 狮7", " 象8"};

            for (int i = 0; i < 7; i++) {
                String map2 = scanner.nextLine();
                String animal2 = scanner1.nextLine();

                for (int j = 0; j < 9; j++) {

                    char m = map2.charAt(j);
                    char a = animal2.charAt(j);
                    int m1 = Integer.parseInt("" + m);
                    int a1 = Integer.parseInt("" + a);
                    map[i][j] = map1[m1];

                    if (j < 4) {
                        animal[i][j] = animalLeft[a1];
                    } else {
                        animal[i][j] = animalRight[a1];
                    }

                }
            }

            output();//  输出地图
            currentStep = 0;
            lastStep = 0;
            playGame();//  游戏进行的过程，并且返回游戏结果game用以下面胜负判断也通过其返回值判断要不要重启游戏或者退出游戏
            String instruction;

            switch (game) {

                case "ExitGame": //  退出游戏

                    System.out.println("游戏退出成功");
                    break loop;

                case "RestartGame": //  重新开始游戏

                    System.out.println("游戏重新开始");
                    continue loop;

                case "GameOver,the Winner is the Right": //  胜负判断

                    System.out.println(game);
                    System.out.println("是否想再战一局");
                    instruction = input.next();
                    if (instruction.equals("restart")) {
                        continue loop;
                    } else
                        break loop;

                case "GameOver,the Winner is the Left": //  胜负判断

                    System.out.println(game);
                    System.out.println("是否想再战一局");
                    instruction = input.next();
                    if (instruction.equals("restart")) {
                        continue loop;
                    } else
                        break loop;
            }

        }

    }

    //  游戏进行过程中的各种判断，实现规则化
    public static void playGame() {
        String[] animalLeft = {" 　 ", "1鼠 ", "2猫 ", "3狼 ", "4狗 ", "5豹 ", "6虎 ", "7狮 ", "8象 "};//  用以将左方的动物存入animal
        String[] animalRight = {" 　 ", " 鼠1", " 猫2", " 狼3", " 狗4", " 豹5", " 虎6", " 狮7", " 象8"};//  用以将右方的动物存入animal
        boolean player = false; //  实现左方先行

        stop:
        while (true) {
            int Eat;//  为了判定一方能否吃掉另一方而带入的辅助变量

            //  每次落子之后将地图储存
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {
                    boardHistory[currentStep][i][j] = animal[i][j];
                }
            }

            String instruction;//  玩家输入的指令

            if (player) {

                System.out.print("请右方玩家输入你的指令：");

            } else {

                System.out.print("请左方玩家输入你的指令：");

            }

            instruction = input.next();
            System.out.println(instruction);

            //  进行玩家的输入判断，并让系统作出相应的动作
            if (instruction.equals("help")) {

                System.out.println("1.移动指令：");
                System.out.println();
                System.out.println("移动指令有两部分组成。");
                System.out.println("第一部分是数字1-8，" + "根据战斗力分别对应鼠(1),猫（2），狼（3），狗（4），豹（5），虎（6）,狮（7），象（8);");
                System.out.println("第二部分是字母wasd,w代表向上移动，a代表向左移动，s代表向下移动，d代表向右移动");
                System.out.println("例如：“1d”表示鼠向右走；“4W表示狗向上走”");
                System.out.println();
                System.out.println("2.游戏指令：");
                System.out.println("输入 restart 重新开始游戏");
                System.out.println("输入 help 查看帮助");
                System.out.println("输入 undo 悔棋");
                System.out.println("输入 redo 取消悔棋");
                System.out.println("输入 exit 退出游戏");
                System.out.println();

                continue;

            } else if (instruction.equals("exit")) {

                game = "ExitGame";
                break;

            } else if (instruction.equals("restart")) {

                game = "RestartGame";
                break;

            } else if (instruction.equals("undo")) {

                //  对是否能悔棋的判断
                if (currentStep == 0) {

                    System.out.println("都已经是初始棋盘了，你还要悔棋，你是不是傻！请重新输入。");
                    continue;

                } else if (currentStep >= 1) {

                    System.out.println("你的愿望实现了（悔棋成功），但是我希望你明白，落棋无悔的人生最完美的。");
                    player = !player;//  保证悔棋之后，玩家能转换
                    undo();
                    continue;

                }

            } else if (instruction.equals("redo")) {

                //  对是否能取消悔棋的判断
                if (lastStep == 0) {

                    System.out.println("弟弟，你没有悔棋过，请不要像ZZ一样想取消悔棋。");
                    continue;

                } else {

                    System.out.println("弟弟，请在你做出决定前，思考思考，不要秀智商下限，你的悔棋已经帮你取消。");
                    redo();
                    player = !player;//  取消悔棋之后的玩家转换
                    continue;

                }

            } else if (instruction.length() != 2) {//  防止不合理的输入长度

                System.out.println("你输入的" + instruction + "的长度不符合要求，请不要秀智商下限，赶紧重新输入。");
                continue;


            }

            String rank = instruction.substring(0, 1);//  动物所代表的数字
            String direction = instruction.substring(1, 2);//  移动方向
            int moveAnimalRow;//  要移动的动物的所在行
            int moveAnimalColumn;//  要移动的动物的所在列

            if (player) {//  玩家判断

                //  判断玩家想要移动的动物是否合理和输入的代表动物的数字是否正确
                if (!(rank.equals("1") || rank.equals("2") || rank.equals("3") || rank.equals("4") || rank.equals("5")
                        || rank.equals("6") || rank.equals("7") || rank.equals("8"))) {

                    System.out.println("你输入的" + rank + "不代表任何动物，你想表达你的智商不够用？请重新输入。");
                    continue;

                } else if (animalRowAndColumn(animalRight[Integer.parseInt(rank)])[0] == -1) {

                    System.out.println("你的动物都被你玩死了，清醒点，加把劲为它报仇，赶紧重新输入指令。");
                    continue;

                } else {

                    moveAnimalRow = animalRowAndColumn(animalRight[Integer.parseInt(rank)])[0];
                    moveAnimalColumn = animalRowAndColumn(animalRight[Integer.parseInt(rank)])[1];

                }

                switch (direction) {//  判断玩家想要移动的方向并且控制动物不会移出界外

                    case "s":
                        if (moveAnimalRow > 5) {

                            System.out.println("亲，你的智商已经趋近-∞，不要妄图走出地图，请重新输入。");
                            continue stop;

                        } else if (moveAnimalRow == 2 && moveAnimalColumn == 8) {//  防止进入自己兽穴

                            System.out.println("你是ZZ？这里是你的兽穴，想自暴自弃？没门，重新输入！");
                            continue stop;

                        } else if (moveTigerLion(moveAnimalRow, moveAnimalColumn, 1, 0, " 狮7", "1鼠 ", " 虎6")) {//  狮虎跳河的功能实现

                            animal[moveAnimalRow + 3][moveAnimalColumn] = animal[moveAnimalRow][moveAnimalColumn];
                            animal[moveAnimalRow][moveAnimalColumn] = " 　 ";
                            lastStep = 0;
                            output();

                        } else if (!animal[moveAnimalRow][moveAnimalColumn].equals(" 鼠1")
                                && map[moveAnimalRow + 1][moveAnimalColumn].equals(" 水 ")) {//  防止别的动物进河

                            if (animal[moveAnimalRow][moveAnimalColumn].equals(" 狮7")
                                    || animal[moveAnimalRow][moveAnimalColumn].equals(" 虎6")) {

                                System.out.println(animal[moveAnimalRow][moveAnimalColumn] + "不满足跳河的条件");
                                continue stop;

                            } else {

                                System.out.println("不是老鼠，无法进河");
                                continue stop;

                            }

                        } else if (judge(moveAnimalRow, moveAnimalColumn, 1, 0, " 鼠1", " 猫2", " 狼3", " 狗4",
                                " 豹5", " 虎6", " 狮7", " 象8")) { //  防止自相残杀

                            System.out.println("亲，不要妄图扮演间谍去杀害自己人，请重新输入。");
                            continue stop;

                        } else if (!animal[moveAnimalRow + 1][moveAnimalColumn].equals(" 　 ")) {
                            Eat = judgeRank(moveAnimalRow, moveAnimalColumn, moveAnimalRow + 1, moveAnimalColumn);

                            //  判断动物的等级和实现吃的功能
                            if ((moveAnimalRow == 1 && moveAnimalColumn == 8) || (moveAnimalRow == 2 && moveAnimalColumn == 7)
                                    || (moveAnimalRow == 3 && moveAnimalColumn == 8)) {

                                move(direction, moveAnimalRow, moveAnimalColumn);
                                lastStep = 0;

                            } else if (Eat == 1 || Eat == 0) {

                                lastStep = 0;
                                move(direction, moveAnimalRow, moveAnimalColumn);

                            } else if (Eat == -1) {

                                System.out.println("弟弟，世间还有许多美好的事物，请不要送死。");
                                continue stop;

                            }

                        } else {

                            move(direction, moveAnimalRow, moveAnimalColumn);
                            lastStep = 0;

                        }
                        break;

                    case "w":
                        if (moveAnimalRow < 1) {

                            System.out.println("亲，你的智商已经趋近-∞，不要妄图走出地图，请重新输入");
                            continue stop;

                        } else if (moveAnimalRow == 4 && moveAnimalColumn == 8) {//  防止进入自己的兽穴

                            System.out.println("你是ZZ？这里是你的兽穴，想自暴自弃？没门，重新输入！");
                            continue stop;

                        } else if (moveTigerLion(moveAnimalRow, moveAnimalColumn, -1, 0, " 狮7", "1鼠 ", " 虎6")) {//  狮虎跳河

                            animal[moveAnimalRow - 3][moveAnimalColumn] = animal[moveAnimalRow][moveAnimalColumn];
                            animal[moveAnimalRow][moveAnimalColumn] = " 　 ";
                            lastStep = 0;
                            output();

                        } else if (!animal[moveAnimalRow][moveAnimalColumn].equals(" 鼠1")
                                && map[moveAnimalRow - 1][moveAnimalColumn].equals(" 水 ")) {//  防止别的动物入河
                            if (animal[moveAnimalRow][moveAnimalColumn].equals(" 狮7")
                                    || animal[moveAnimalRow][moveAnimalColumn].equals(" 虎6")) {

                                System.out.println(animal[moveAnimalRow][moveAnimalColumn] + "不满足跳河的条件");
                                continue stop;

                            } else {

                                System.out.println("不是老鼠，无法进河");
                                continue stop;

                            }

                        } else if (judge(moveAnimalRow, moveAnimalColumn, -1, 0, " 鼠1", " 猫2", " 狼3", " 狗4",
                                " 豹5", " 虎6", " 狮7", " 象8")) {//  防止自相残杀

                            System.out.println("亲，不要妄图扮演间谍去杀害自己人，请重新输入。");
                            continue stop;

                        } else if (!animal[moveAnimalRow - 1][moveAnimalColumn].equals(" 　 ")) {
                            Eat = judgeRank(moveAnimalRow, moveAnimalColumn, moveAnimalRow - 1, moveAnimalColumn);

                            //  判断动物的等级和实现吃的功能
                            if ((moveAnimalRow == 3 && moveAnimalColumn == 8) || (moveAnimalRow == 4 && moveAnimalColumn == 7)
                                    || (moveAnimalRow == 5 && moveAnimalColumn == 8)) {

                                move(direction, moveAnimalRow, moveAnimalColumn);
                                lastStep = 0;

                            } else if (Eat == 1 || Eat == 0) {

                                lastStep = 0;
                                move(direction, moveAnimalRow, moveAnimalColumn);

                            } else if (Eat == -1) {

                                System.out.println("弟弟，世间还有许多美好的事物，请不要送死。");
                                continue stop;

                            }

                        } else {

                            lastStep = 0;
                            move(direction, moveAnimalRow, moveAnimalColumn);

                        }
                        break;

                    case "a":
                        if (moveAnimalColumn < 1) {

                            System.out.println("亲，你的智商已经趋近-∞，不要妄图走出地图，请重新输入");
                            continue stop;

                        } else if (moveTigerLion(moveAnimalRow, moveAnimalColumn, 0, -1, " 狮7", "1鼠 ", " 虎6")) {//  狮虎跳河

                            animal[moveAnimalRow][moveAnimalColumn - 4] = animal[moveAnimalRow][moveAnimalColumn];
                            animal[moveAnimalRow][moveAnimalColumn] = " 　 ";
                            lastStep = 0;
                            output();

                        } else if (!animal[moveAnimalRow][moveAnimalColumn].equals(" 鼠1")
                                && map[moveAnimalRow][moveAnimalColumn - 1].equals(" 水 ")) {//  防止别的动物进河

                            if (animal[moveAnimalRow][moveAnimalColumn].equals(" 狮7")
                                    || animal[moveAnimalRow][moveAnimalColumn].equals(" 虎6")) {

                                System.out.println(animal[moveAnimalRow][moveAnimalColumn] + "不满足跳河的条件");
                                continue stop;

                            } else {

                                System.out.println("不是老鼠，无法进河");
                                continue stop;

                            }

                        } else if (judge(moveAnimalRow, moveAnimalColumn, 0, -1, " 鼠1", " 猫2", " 狼3", " 狗4",
                                " 豹5", " 虎6", " 狮7", " 象8")) {//  防止自相残杀

                            System.out.println("亲，不要妄图扮演间谍去杀害自己人，请重新输入。");
                            continue stop;

                        } else if (!animal[moveAnimalRow][moveAnimalColumn - 1].equals(" 　 ")) {
                            Eat = judgeRank(moveAnimalRow, moveAnimalColumn, moveAnimalRow, moveAnimalColumn - 1);

                            //  判断动物的等级和实现吃的功能
                            if (Eat == 1 || Eat == 0) {

                                lastStep = 0;
                                move(direction, moveAnimalRow, moveAnimalColumn);

                            } else if (Eat == -1) {

                                System.out.println("弟弟，世间还有许多美好的事物，请不要送死。");
                                continue stop;

                            }

                        } else {

                            lastStep = 0;
                            move(direction, moveAnimalRow, moveAnimalColumn);

                        }
                        break;

                    case "d":
                        if (moveAnimalColumn > 7) {

                            System.out.println("亲，你的智商已经趋近-∞，不要妄图走出地图，请重新输入");
                            continue stop;

                        } else if (moveTigerLion(moveAnimalRow, moveAnimalColumn, 0, 1, " 狮7", "1鼠 ", " 虎6")) {//  狮虎跳河

                            animal[moveAnimalRow][moveAnimalColumn + 4] = animal[moveAnimalRow][moveAnimalColumn];
                            animal[moveAnimalRow][moveAnimalColumn] = " 　 ";
                            lastStep = 0;
                            output();

                        } else if (!animal[moveAnimalRow][moveAnimalColumn].equals(" 鼠1")
                                && map[moveAnimalRow][moveAnimalColumn + 1].equals(" 水 ")) {//  防止别的动物进河

                            if (animal[moveAnimalRow][moveAnimalColumn].equals(" 狮7")
                                    || animal[moveAnimalRow][moveAnimalColumn].equals(" 虎6")) {

                                System.out.println(animal[moveAnimalRow][moveAnimalColumn] + "不满足跳河的条件");
                                continue stop;

                            } else {

                                System.out.println("不是老鼠，无法进河");
                                continue stop;

                            }

                        } else if (judge(moveAnimalRow, moveAnimalColumn, 0, 1, " 鼠1", " 猫2", " 狼3", " 狗4",
                                " 豹5", " 虎6", " 狮7", " 象8")) {//  防止自相残杀

                            System.out.println("亲，不要妄图扮演间谍去杀害自己人，请重新输入。");
                            continue stop;

                        } else if (moveAnimalRow == 3 && moveAnimalColumn == 7) {//  防止进入自己的兽穴

                            System.out.println("你是ZZ？这里是你的兽穴，想自暴自弃？没门，重新输入！");
                            continue stop;

                        } else if (!animal[moveAnimalRow][moveAnimalColumn + 1].equals(" 　 ")) {
                            Eat = judgeRank(moveAnimalRow, moveAnimalColumn, moveAnimalRow, moveAnimalColumn + 1);

                            //  判断动物的等级和实现吃的功能
                            if ((moveAnimalRow == 2 && moveAnimalColumn == 7) || (moveAnimalRow == 3
                                    && moveAnimalColumn == 6) || (moveAnimalRow == 4 && moveAnimalColumn == 7)) {

                                move(direction, moveAnimalRow, moveAnimalColumn);
                                lastStep = 0;

                            } else if (Eat == 1 || Eat == 0) {

                                lastStep = 0;
                                move(direction, moveAnimalRow, moveAnimalColumn);

                            } else if (Eat == -1) {

                                System.out.println("弟弟，世间还有许多美好的事物，请不要送死。");
                                continue stop;

                            }

                        } else {

                            lastStep = 0;
                            move(direction, moveAnimalRow, moveAnimalColumn);

                        }
                        break;

                    default:

                        System.out.println("你输入的" + direction + "不对应任何方向，你是来搞笑的？请重新输入");
                        continue stop;

                }

                //  胜负判断
                if (judgeStalemate("1鼠 ", "2猫 ", "3狼 ", "4狗 ", "5豹 ", "6虎 ", "7狮 ", "8象 ")[1] ==
                        judgeStalemate("1鼠 ", "2猫 ", "3狼 ", "4狗 ", "5豹 ", "6虎 ", "7狮 ", "8象 ")[0]
                        && judgeStalemate("1鼠 ", "2猫 ", "3狼 ", "4狗 ", "5豹 ", "6虎 ", "7狮 ", "8象 ")[1] != 0) {

                    game = "GameOver,the Winner is the Right";
                    break;

                } else if (judgeStalemate("1鼠 ", "2猫 ", "3狼 ", "4狗 ", "5豹 ", "6虎 ", "7狮 ", "8象 ")[1] == 0) {

                    game = "GameOver,the Winner is the Right";
                    break;

                } else if (!animal[3][0].equals(" 　 ")) {

                    game = "GameOver,the Winner is the Right";
                    break;

                }

                currentStep++;//  保证落子之前储存的地图不被落子之后的地图覆盖，记录落子次数（地图的数目）
                player = !player;//  转换玩家

            } else {

                //  判断玩家想要移动的动物是否合理和输入的代表动物的数字是否正确
                if (!(rank.equals("1") || rank.equals("2") || rank.equals("3") || rank.equals("4") || rank.equals("5")
                        || rank.equals("6") || rank.equals("7") || rank.equals("8"))) {

                    System.out.println("你输入的" + rank + "不代表任何动物，你想表达你的智商不够用？请重新输入。");
                    continue;

                } else if (animalRowAndColumn(animalLeft[Integer.parseInt(rank)])[0] == -1) {

                    System.out.println("你的动物都被你玩死了，清醒点，加把劲为它报仇，赶紧重新输入指令。");
                    continue;

                } else {

                    moveAnimalRow = animalRowAndColumn(animalLeft[Integer.parseInt(rank)])[0];
                    moveAnimalColumn = animalRowAndColumn(animalLeft[Integer.parseInt(rank)])[1];

                }

                switch (direction) {//  判断玩家想要移动的方向并且保证动物不会越界
                    case "s":
                        if (moveAnimalRow > 5) {

                            System.out.println("亲，你的智商已经趋近-∞，不要妄图走出地图，请重新输入");
                            continue stop;

                        } else if (moveTigerLion(moveAnimalRow, moveAnimalColumn, 1, 0, "7狮 ", " 鼠1", "6虎 ")) {//  狮虎跳河

                            animal[moveAnimalRow + 3][moveAnimalColumn] = animal[moveAnimalRow][moveAnimalColumn];
                            animal[moveAnimalRow][moveAnimalColumn] = " 　 ";
                            lastStep = 0;
                            output();

                        } else if (!animal[moveAnimalRow][moveAnimalColumn].equals("1鼠 ")
                                && map[moveAnimalRow + 1][moveAnimalColumn].equals(" 水 ")) {//防止别的动物进河

                            if (animal[moveAnimalRow][moveAnimalColumn].equals("7狮 ")
                                    || animal[moveAnimalRow][moveAnimalColumn].equals("6虎 ")) {

                                System.out.println(animal[moveAnimalRow][moveAnimalColumn] + "不满足跳河的条件");
                                continue stop;

                            } else {

                                System.out.println("不是老鼠，无法进河");
                                continue stop;

                            }

                        } else if (judge(moveAnimalRow, moveAnimalColumn, 1, 0, "1鼠 ", "2猫 ", "3狼 ", "4狗 ",
                                "5豹 ", "6虎 ", "7狮 ", "8象 ")) {//  防止自相残杀

                            System.out.println("亲，不要妄图扮演间谍去杀害自己人，请重新输入。");
                            continue stop;

                        } else if (moveAnimalRow == 2 && moveAnimalColumn == 0) {//  防止进入自己的兽穴

                            System.out.println("你是ZZ？这里是你的兽穴，想自暴自弃？没门，重新输入！");
                            continue stop;

                        } else if (!animal[moveAnimalRow + 1][moveAnimalColumn].equals(" 　 ")) {
                            Eat = judgeRank(moveAnimalRow + 1, moveAnimalColumn, moveAnimalRow, moveAnimalColumn);

                            //  判断动物的等级和实现吃的功能
                            if ((moveAnimalRow == 1 && moveAnimalColumn == 0) || (moveAnimalRow == 2 && moveAnimalColumn == 1)
                                    || (moveAnimalRow == 3 && moveAnimalColumn == 0)) {

                                move(direction, moveAnimalRow, moveAnimalColumn);
                                lastStep = 0;

                            } else if (Eat == -1 || Eat == 0) {

                                move(direction, moveAnimalRow, moveAnimalColumn);
                                lastStep = 0;

                            } else if (Eat == 1) {

                                System.out.println("弟弟，世间还有许多美好的事物，请不要送死。");
                                continue stop;

                            }

                        } else {

                            lastStep = 0;
                            move(direction, moveAnimalRow, moveAnimalColumn);

                        }
                        break;

                    case "w":
                        if (moveAnimalRow < 1) {

                            System.out.println("亲，你的智商已经趋近-∞，不要妄图走出地图，请重新输入");
                            continue stop;

                        } else if (moveTigerLion(moveAnimalRow, moveAnimalColumn, -1, 0, "7狮 ", " 鼠1", "6虎 ")) {//  狮虎跳河

                            animal[moveAnimalRow - 3][moveAnimalColumn] = animal[moveAnimalRow][moveAnimalColumn];
                            animal[moveAnimalRow][moveAnimalColumn] = " 　 ";
                            lastStep = 0;
                            output();

                        } else if (!animal[moveAnimalRow][moveAnimalColumn].equals("1鼠 ")
                                && map[moveAnimalRow - 1][moveAnimalColumn].equals(" 水 ")) {//  防止别的动物进河

                            if (animal[moveAnimalRow][moveAnimalColumn].equals("7狮 ")
                                    || animal[moveAnimalRow][moveAnimalColumn].equals("6虎 ")) {

                                System.out.println(animal[moveAnimalRow][moveAnimalColumn] + "不满足跳河的条件");
                                continue stop;

                            } else {

                                System.out.println("不是老鼠，无法进河");
                                continue stop;

                            }

                        } else if (judge(moveAnimalRow, moveAnimalColumn, -1, 0, "1鼠 ", "2猫 ", "3狼 ", "4狗 ",
                                "5豹 ", "6虎 ", "7狮 ", "8象 ")) {//  防止自相残杀

                            System.out.println("亲，不要妄图扮演间谍去杀害自己人，请重新输入。");
                            continue stop;

                        } else if (moveAnimalRow == 4 && moveAnimalColumn == 0) {//  防止进入自己的兽穴

                            System.out.println("你是ZZ？这里是你的兽穴，想自暴自弃？没门，重新输入！");
                            continue stop;

                        } else if (!animal[moveAnimalRow - 1][moveAnimalColumn].equals(" 　 ")) {
                            Eat = judgeRank(moveAnimalRow - 1, moveAnimalColumn, moveAnimalRow, moveAnimalColumn);

                            //  判断动物的等级和实现吃的功能
                            if ((moveAnimalRow == 3 && moveAnimalColumn == 0) || (moveAnimalRow == 4 && moveAnimalColumn == 1)
                                    || (moveAnimalRow == 5 && moveAnimalColumn == 0)) {

                                move(direction, moveAnimalRow, moveAnimalColumn);
                                lastStep = 0;

                            } else if (Eat == -1 || Eat == 0) {

                                lastStep = 0;
                                move(direction, moveAnimalRow, moveAnimalColumn);

                            } else if (Eat == 1) {

                                System.out.println("弟弟，世间还有许多美好的事物，请不要送死。");
                                continue stop;

                            }

                        } else {

                            lastStep = 0;
                            move(direction, moveAnimalRow, moveAnimalColumn);

                        }
                        break;

                    case "d":
                        if (moveAnimalColumn > 7) {

                            System.out.println("亲，你的智商已经趋近-∞，不要妄图走出地图，请重新输入");
                            continue stop;

                        } else if (moveTigerLion(moveAnimalRow, moveAnimalColumn, 0, 1, "7狮 ", " 鼠1", "6虎 ")) {//  狮虎跳河

                            animal[moveAnimalRow][moveAnimalColumn + 4] = animal[moveAnimalRow][moveAnimalColumn];
                            animal[moveAnimalRow][moveAnimalColumn] = " 　 ";
                            lastStep = 0;
                            output();

                        } else if (!animal[moveAnimalRow][moveAnimalColumn].equals("1鼠 ")
                                && map[moveAnimalRow][moveAnimalColumn + 1].equals(" 水 ")) {//  防止别的动物进河

                            if (animal[moveAnimalRow][moveAnimalColumn].equals("7狮 ")
                                    || animal[moveAnimalRow][moveAnimalColumn].equals("6虎 ")) {

                                System.out.println(animal[moveAnimalRow][moveAnimalColumn] + "不满足跳河的条件");
                                continue stop;

                            } else {

                                System.out.println("不是老鼠，无法进河");
                                continue stop;

                            }

                        } else if (judge(moveAnimalRow, moveAnimalColumn, 0, 1, "1鼠 ", "2猫 ", "3狼 ", "4狗 ",
                                "5豹 ", "6虎 ", "7狮 ", "8象 ")) {//  防止自相残杀

                            System.out.println("亲，不要妄图扮演间谍去杀害自己人，请重新输入。");
                            continue stop;

                        } else if (!animal[moveAnimalRow][moveAnimalColumn + 1].equals(" 　 ")) {
                            Eat = judgeRank(moveAnimalRow, moveAnimalColumn + 1, moveAnimalRow, moveAnimalColumn);

                            //  判断动物的等级和实现吃的功能
                            if (Eat == -1 || Eat == 0) {

                                lastStep = 0;
                                move(direction, moveAnimalRow, moveAnimalColumn);

                            } else if (Eat == 1) {

                                System.out.println("弟弟，世间还有许多美好的事物，请不要送死。");
                                continue stop;

                            }

                        } else {

                            lastStep = 0;
                            move(direction, moveAnimalRow, moveAnimalColumn);

                        }
                        break;

                    case "a":
                        if (moveAnimalColumn < 1) {

                            System.out.println("亲，你的智商已经趋近-∞，不要妄图走出地图，请重新输入");
                            continue stop;

                        } else if (moveTigerLion(moveAnimalRow, moveAnimalColumn, 0, -1, "7狮 ", " 鼠1", "6虎 ")) {//  狮虎跳河

                            animal[moveAnimalRow][moveAnimalColumn - 4] = animal[moveAnimalRow][moveAnimalColumn];
                            animal[moveAnimalRow][moveAnimalColumn] = " 　 ";
                            lastStep = 0;
                            output();

                        } else if (!animal[moveAnimalRow][moveAnimalColumn].equals("1鼠 ")
                                && map[moveAnimalRow][moveAnimalColumn - 1].equals(" 水 ")) {//  防止别的动物进河

                            if (animal[moveAnimalRow][moveAnimalColumn].equals("7狮 ")
                                    || animal[moveAnimalRow][moveAnimalColumn].equals("6虎 ")) {

                                System.out.println(animal[moveAnimalRow][moveAnimalColumn] + "不满足跳河的条件");
                                continue stop;

                            } else {

                                System.out.println("不是老鼠，无法进河");
                                continue stop;

                            }

                        } else if (judge(moveAnimalRow, moveAnimalColumn, 0, -1, "1鼠 ", "2猫 ", "3狼 ", "4狗 ",
                                "5豹 ", "6虎 ", "7狮 ", "8象 ")) {//  防止自相残杀

                            System.out.println("亲，不要妄图扮演间谍去杀害自己人，请重新输入。");
                            continue stop;

                        } else if (moveAnimalRow == 3 && moveAnimalColumn == 1) {//  防止进入自己的兽穴

                            System.out.println("你是ZZ？这里是你的兽穴，想自暴自弃？没门，重新输入！");
                            continue stop;

                        } else if (!animal[moveAnimalRow][moveAnimalColumn - 1].equals(" 　 ")) {
                            Eat = judgeRank(moveAnimalRow, moveAnimalColumn - 1, moveAnimalRow, moveAnimalColumn);

                            //  判断动物的等级和实现吃的功能
                            if ((moveAnimalRow == 2 && moveAnimalColumn == 1) || (moveAnimalRow == 3 && moveAnimalColumn == 2)
                                    || (moveAnimalRow == 4 && moveAnimalColumn == 1)) {

                                move(direction, moveAnimalRow, moveAnimalColumn);
                                lastStep = 0;

                            } else if (Eat == -1 || Eat == 0) {

                                lastStep = 0;
                                move(direction, moveAnimalRow, moveAnimalColumn);

                            } else if (Eat == 1) {

                                System.out.println("弟弟，世间还有许多美好的事物，请不要送死。");
                                continue stop;

                            }

                        } else {

                            lastStep = 0;
                            move(direction, moveAnimalRow, moveAnimalColumn);

                        }
                        break;

                    default:

                        System.out.println("你输入的" + direction + "不对应任何方向，你是来搞笑的？请重新输入");
                        continue stop;

                }

                //  胜负判断
                if (judgeStalemate(" 鼠1", " 猫2", " 狼3", " 狗4", " 豹5", " 虎6", " 狮7", " 象8")[1] ==
                        judgeStalemate(" 鼠1", " 猫2", " 狼3", " 狗4", " 豹5", " 虎6", " 狮7", " 象8")[0]
                        && judgeStalemate(" 鼠1", " 猫2", " 狼3", " 狗4", " 豹5", " 虎6", " 狮7", " 象8")[1] != 0) {

                    game = "GameOver,the Winner is the Left";
                    break;

                } else if (judgeStalemate(" 鼠1", " 猫2", " 狼3", " 狗4", " 豹5", " 虎6", " 狮7", " 象8")[1] == 0) {

                    game = "GameOver,the Winner is the Left";
                    break;

                } else if (!animal[3][8].equals(" 　 ")) {

                    game = "GameOver,the Winner is the Left";
                    break;

                }

                currentStep++;//  记录落子次数（地图的数目），保证落子之前储存的地图不被落子之后的地图覆盖
                player = !player;//  玩家转换
            }
        }
    }

    //  打印棋盘
    public static void output() {
        System.out.println("亲，如果你不明白规则，输入help可以让你接触到斗兽棋之新手篇。");
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (animal[i][j].equals(" 　 ")) {
                    System.out.print(map[i][j]);
                } else {
                    System.out.print(animal[i][j]);
                }
            }
            System.out.println();
        }
    }

    //  获取想要移动的动物的坐标
    public static int[] animalRowAndColumn(String animal) {
        int[] animalRC = {-1, -1};
        stop:
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (AnimalChecker.animal[i][j].equals(animal)) {
                    animalRC[0] = i;
                    animalRC[1] = j;
                    break stop;
                }
            }
        }
        return animalRC;
    }

    //  判断要走的地方有没有队友，a、b、c、d、e、f、g、h代表同一阵营的八个动物
    public static boolean judge(int moveAnimalRow, int moveAnimalColumn, int x, int y, String a, String b, String c, String d, String e, String f, String g, String h) {
        boolean i = false;

        if (animal[moveAnimalRow + x][moveAnimalColumn + y].equals(a)
                || animal[moveAnimalRow + x][moveAnimalColumn + y].equals(b)
                || animal[moveAnimalRow + x][moveAnimalColumn + y].equals(c)
                || animal[moveAnimalRow + x][moveAnimalColumn + y].equals(d)
                || animal[moveAnimalRow + x][moveAnimalColumn + y].equals(e)
                || animal[moveAnimalRow + x][moveAnimalColumn + y].equals(f)
                || animal[moveAnimalRow + x][moveAnimalColumn + y].equals(g)
                || animal[moveAnimalRow + x][moveAnimalColumn + y].equals(h)) {

            i = true;

        }
        return i;
    }

    //狮虎能否跳河的判断
    public static boolean moveTigerLion(int moveAnimalRow, int moveAnimalColumn, int x, int y, String lion, String enemyMouse, String tiger) {
        boolean i = false;
        if (animal[moveAnimalRow][moveAnimalColumn].equals(lion)
                && map[moveAnimalRow + x][moveAnimalColumn].equals(" 水 ")//  判断前面是否有水
                && !(animal[moveAnimalRow + x][moveAnimalColumn].equals(enemyMouse)
                || animal[moveAnimalRow + 2 * x][moveAnimalColumn].equals(enemyMouse))//  判断是否有对方老鼠在所在行
                || animal[moveAnimalRow][moveAnimalColumn].equals(tiger)
                && map[moveAnimalRow + x][moveAnimalColumn].equals(" 水 ")//  判断前面是不是水
                && !(animal[moveAnimalRow + x][moveAnimalColumn].equals(enemyMouse)//  判断是否有对方老鼠在所在行
                || animal[moveAnimalRow + 2 * x][moveAnimalColumn].equals(enemyMouse))) {

            //  判断能否跳河
            if (animal[moveAnimalRow + 3 * x][moveAnimalColumn].equals(" 　 ")) {

                i = true;

            } else if ((animal[moveAnimalRow][moveAnimalColumn].substring(0, 1).equals(" ")
                    && judgeRank(moveAnimalRow, moveAnimalColumn, moveAnimalRow + 3 * x, moveAnimalColumn) >= 0)
                    || (animal[moveAnimalRow][moveAnimalColumn].substring(2, 3).equals(" ")
                    && judgeRank(moveAnimalRow + 3 * x, moveAnimalColumn, moveAnimalRow, moveAnimalColumn) <= 0)) {

                i = true;

            }

        } else if (animal[moveAnimalRow][moveAnimalColumn].equals(lion)
                && map[moveAnimalRow][moveAnimalColumn + y].equals(" 水 ")//  判断前面是不是水
                && !(animal[moveAnimalRow][moveAnimalColumn + y].equals(enemyMouse)
                || animal[moveAnimalRow][moveAnimalColumn + 2 * y].equals(enemyMouse)
                || animal[moveAnimalRow][moveAnimalColumn + 3 * y].equals(enemyMouse))//  判断所在列有没有对方的老鼠
                || animal[moveAnimalRow][moveAnimalColumn].equals(tiger)
                && map[moveAnimalRow][moveAnimalColumn + y].equals(" 水 ")//  判断前面是不是水
                && !(animal[moveAnimalRow][moveAnimalColumn + y].equals(enemyMouse)//  判断所在列有没有对方的老鼠
                || animal[moveAnimalRow][moveAnimalColumn + 2 * y].equals(enemyMouse)
                || animal[moveAnimalRow][moveAnimalColumn + 3 * y].equals(enemyMouse))) {

            //  判断能否跳河
            if (animal[moveAnimalRow][moveAnimalColumn + 4 * y].equals(" 　 ")) {

                i = true;

            } else if ((animal[moveAnimalRow][moveAnimalColumn].substring(0, 1).equals(" ")
                    && judgeRank(moveAnimalRow, moveAnimalColumn, moveAnimalRow, moveAnimalColumn + 4 * y) >= 0)
                    || (animal[moveAnimalRow][moveAnimalColumn].substring(2, 3).equals(" ")
                    && judgeRank(moveAnimalRow, moveAnimalColumn + 4 * y, moveAnimalRow, moveAnimalColumn) <= 0)) {

                i = true;

            }
        }
        return i;
    }

    //  移动
    public static void move(String direction, int row, int column) {

        switch (direction) {
            case "s":

                animal[row + 1][column] = animal[row][column];
                animal[row][column] = " 　 ";
                output();
                break;

            case "w":

                animal[row - 1][column] = animal[row][column];
                animal[row][column] = " 　 ";
                output();
                break;

            case "a":

                animal[row][column - 1] = animal[row][column];
                animal[row][column] = " 　 ";
                output();
                break;

            case "d":

                animal[row][column + 1] = animal[row][column];
                animal[row][column] = " 　 ";
                output();
                break;

        }
    }

    //  判断动物的等级
    public static int judgeRank(int rightAnimalRow, int rightAnimalColumn, int leftAnimalRow, int leftAnimalColumn) {
        int rightRank = animal[rightAnimalRow][rightAnimalColumn].charAt(2) - '0';//  将代表的等级的字符串转化成数字
        int leftRank = animal[leftAnimalRow][leftAnimalColumn].charAt(0) - '0';//  将代表的等级的字符串转化成数字
        int i;//  代表判断结果的变量

        if (rightRank == 1 && leftRank == 8) {

            if (map[rightAnimalRow][rightAnimalColumn].equals(" 水 ")) {
                i = -1;
            } else {
                i = 1;
            }

        } else if (rightRank == 8 && leftRank == 1) {

            if (map[leftAnimalRow][leftAnimalColumn].equals(" 水 ")) {
                i = 1;
            } else {
                i = -1;
            }

        } else if (rightRank > leftRank) {
            i = 1;
        } else if (rightRank < leftRank)
            i = -1;
        else
            i = 0;
        return i;
    }

    //  实现悔棋
    public static void undo() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                boardBeforeUndo[lastStep][i][j] = boardHistory[currentStep][i][j];//  将悔棋的地图存入取消悔棋的数组中
            }
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                animal[i][j] = boardHistory[currentStep - 1][i][j];//  将之前的地图重现
            }
        }

        lastStep++;
        currentStep--;
        output();

    }

    //  取消悔棋
    public static void redo() {
        System.out.println("取消悔棋成功!");

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                animal[i][j] = boardBeforeUndo[lastStep - 1][i][j];//  将悔棋之前的地图重现，取消悔棋
            }
        }

        currentStep++;
        lastStep--;
        output();

    }

    //  判断动物是否能够移动
    public static boolean judgeAnimalMove(int animalRow, int animalColumn, int x, int y) {
        //  对移动是否会导致越界进行判断
        if (animalColumn + y == 9) {
            return true;
        } else if (animalColumn + y == -1) {
            return true;
        } else if (animalRow + x == -1) {
            return true;
        } else if (animalRow + x == 7) {
            return true;
        }
        //  对移动是否会导致进去我方兽穴进行判断
        if (animal[animalRow][animalColumn].substring(2, 3).equals(" ") && animalRow + x == 3 && animalColumn + y == 0) {
            return true;
        }
        if (animal[animalRow][animalColumn].substring(0, 1).equals(" ") && animalRow + x == 3 && animalColumn + y == 8) {
            return true;
        }
        //  移动到地方是水的时候的判断
        if (map[animalRow + x][animalColumn + y].equals(" 水 ")) {
            //  对狮子和老虎的判断
            if (animal[animalRow][animalColumn].equals(" 狮7") || animal[animalRow][animalColumn].equals(" 虎6")) {
                if (!moveTigerLion(animalRow, animalColumn, x, y, " 狮7", "1鼠 ", " 虎6")) {
                    return true;
                }
            } else if ((animal[animalRow][animalColumn].equals("7狮 ") || animal[animalRow][animalColumn].equals("6虎 "))) {
                if (!moveTigerLion(animalRow, animalColumn, x, y, "7狮 ", " 鼠1", "6虎 "))
                    return true;
            } else if (!animal[animalRow][animalColumn].substring(1, 2).equals("鼠")) {
                return true;
            }
        }
        //  移动的地方是空地的判断
        if (animal[animalRow][animalColumn].equals(" 　 ")) {
            return false;
        }
        if (!animal[animalRow + x][animalColumn + y].equals(" 　 ")) {
            //  对移动是否会导致自相残杀的判断
            if (animal[animalRow + x][animalColumn + y].substring(0, 1).equals(animal[animalRow][animalColumn].substring(0, 1))) {
                return true;
            } else if (animal[animalRow + x][animalColumn + y].substring(2, 3).equals(animal[animalRow][animalColumn].substring(2, 3))) {
                return true;
            }
            //  对移动是否会导致送死的判断
            if (!animal[animalRow + x][animalColumn + y].substring(0, 1).equals(animal[animalRow][animalColumn].substring(0, 1))
                    && animal[animalRow][animalColumn].substring(0, 1).equals(" ")) {

                if (judgeRank(animalRow, animalColumn, animalRow + x, animalColumn + y) == -1) {
                    return true;
                }

            } else if (!animal[animalRow + x][animalColumn + y].substring(0, 1).equals(animal[animalRow][animalColumn].substring(0, 1))
                    && animal[animalRow][animalColumn].substring(2, 3).equals(" ")) {

                if (judgeRank(animalRow + x, animalColumn + y, animalRow, animalColumn) == 1) {
                    return true;
                }

            }

        }
        return false;
    }

    //  判断一方是否已经无子可动
    public static int[] judgeStalemate(String a, String b, String c, String d, String e, String f, String g, String h) {
        int[] judgeArray = new int[2];
        judgeArray[0] = 0;//  judgeArray[0]代表不能移动的动物的个数
        judgeArray[1] = 0;//  judgeArray[1]代表存活的动物个数
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {

                if (animal[i][j].equals(a) || animal[i][j].equals(b)
                        || animal[i][j].equals(c) || animal[i][j].equals(d)
                        || animal[i][j].equals(e) || animal[i][j].equals(f)
                        || animal[i][j].equals(g) || animal[i][j].equals(h)) {
                    judgeArray[1]++;
                    if (judgeAnimalMove(i, j, 0, 1) && judgeAnimalMove(i, j, 0, -1) && judgeAnimalMove(i, j, 1, 0) && judgeAnimalMove(i, j, -1, 0)) {
                        judgeArray[0]++;
                    }

                }
            }
        }
//        return judgeArray[1] == judgeArray[0] && judgeArray[1] != 0;
        return judgeArray;
    }
}


