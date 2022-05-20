import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Program {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        System.out.println("Выполнить эксперимент перед началом программы?(yes/no)");
        var scanner = new Scanner(System.in);
        var input = scanner.next();
        if (input.equals("yes")) {
            var arr = ReadXML("experiment.xml");
            for (var i = 0; i < arr.size(); i++) {
                writeToFile(RedBlackSort(arr.get(i)));
            }
        }

        System.out.println("\nВведите числа для сортировки, разделяя их пробелом:");
        var tree = new RedBlackTree();
        scanner.nextLine();
        var inpArr = scanner.nextLine().split(" ");
        for (String str : inpArr) {
            tree.insert(Integer.parseInt(str));
        }
        System.out.println("Отсортированный массив чисел: ");
        tree.inorder();
    }


    private static ArrayList<ArrayList<Integer>> RedBlackSort(String values) {
        var outputData = new ArrayList<ArrayList<Integer>>(); // 0 - current array length, 1 - current tree count if uses
        outputData.add(0, new ArrayList<>());
        outputData.add(1, new ArrayList<>());
        var input = values.replace('[', ' ').replace(']', ' ').trim().split(", ");
        String name = input[0];
        int minElement = Integer.parseInt(input[1]),
                maxElement = Integer.parseInt(input[2]),
                startLength = Integer.parseInt(input[3]),
                maxLength = Integer.parseInt(input[4]),
                repeat = Integer.parseInt(input[5]),
                diff = Integer.parseInt(input[6]),
                znamen = Integer.parseInt(input[7]);
        System.out.println('\n' + "Эксперимент: " + name);
        Random rnd = new Random();
        var tree = new RedBlackTree();
        var allCount = 0;
        var avgCount = 0;
        if (name.equals("Arithmetic Progression")) {
            for (var i = startLength; i <= maxLength; i += diff) { //размер всего массива
                System.out.print("Длина массива:" + i + "\t" + "Кол-во массивов:" + repeat + "\t");
                for (var j = 1; j <= repeat; j++) { //количество попыток для вычисления среднего

                    for (var length = 1; length <= i; length++) { //добавить элементы количеством startLength в дерево
                        tree.insert(rnd.nextInt(minElement, maxElement + 1));
                    }
                    allCount += tree.count;
                    tree.count = 0;
                }
                avgCount = allCount / repeat;
                outputData.get(0).add(i);
                outputData.get(1).add(avgCount);
                System.out.print("Операций в среднем:" + avgCount);
                System.out.println();
                allCount = 0;
            }
        } else if (name.equals("Geometric Progression")) {
            for (var i = startLength; i <= maxLength; i *= znamen) { //размер всего массива
                System.out.print("Длина массива:" + i + "\t" + "Кол-во массивов:" + repeat + "\t");
                for (var j = 1; j <= repeat; j++) { //количество попыток для вычисления среднего

                    for (var length = 1; length <= i; length++) { //добавить элементы количеством startLength в дерево
                        tree.insert(rnd.nextInt(minElement, maxElement + 1));
                    }
                    allCount += tree.count;
                    tree.count = 0;
                }
                avgCount = allCount / repeat;
                outputData.get(0).add(i);
                outputData.get(1).add(avgCount);
                System.out.print("Операций в среднем:" + avgCount);
                System.out.println();
                allCount = 0;
            }
        }
        return outputData;
    }

    private static ArrayList<String> ReadXML(String path) throws ParserConfigurationException, IOException, SAXException {
        var docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        var sb = new StringBuilder();
        var result = new ArrayList<String>();
        // Создается дерево DOM документа из файла
        var xmlDoc = docBuilder.parse(path);
        // Получаем корневой элемент
        Node root = xmlDoc.getDocumentElement();
        root = root.getChildNodes().item(1);
        System.out.println("\t\t\t" + root.getAttributes().getNamedItem("name").getNodeValue()); // Red-black tree
        var nodes = root.getChildNodes();
        // Просматриваем все подэлементы корневого элемента
        for (var i = 1; i < nodes.getLength(); i += 2) {///!!ДО NODES.LENGTH
            var name = nodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
            var minElement = Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("minElement").getNodeValue());
            var maxElement = Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("maxElement").getNodeValue());
            var startLength = Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("startLength").getNodeValue());
            var maxLength = Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("maxLength").getNodeValue());
            var repeat = Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("repeat").getNodeValue());
            var diff = 0;
            var znamen = 1;

            if (name.equals("Arithmetic Progression")) {
                diff = Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("diff").getNodeValue());
                //RedBlackSort(name, minElement, maxElement, startLength, maxLength, repeat, diff, znamen);
            } else if (name.equals("Geometric Progression")) {
                znamen = Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("znamen").getNodeValue());
                //RedBlackSort(name, minElement, maxElement, startLength, maxLength, repeat, diff, znamen);
            } else {
                //System.out.println('\n' + "Эксперимент: " + name);
                //RedBlackSort(name, minElement, maxElement, startLength, maxLength, repeat, diff, znamen);
            }
            sb.append(Arrays.asList(name, minElement, maxElement, startLength, maxLength, repeat, diff, znamen));
            result.add(sb.toString());
            sb.delete(0, sb.length());
        }
        return result;
    }

    private static void writeToFile(ArrayList<ArrayList<Integer>> outputData) throws IOException {
        FileWriter writer = new FileWriter("output.txt", true);
        for (var i = 0; i < outputData.get(0).size(); i++) {
            writer.append(outputData.get(0).get(i) + "\t" + outputData.get(1).get(i) + "\n");
        }
        writer.close();
    }
}
