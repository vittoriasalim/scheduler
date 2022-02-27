import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Timetable {

    private ArrayList<ArrayList<Unit>> choices;

    public Timetable() {
        this.choices = new ArrayList<ArrayList<Unit>>();
    }

    // Function to print the root to leaf path of the given N-ary Tree
    public void printPath(ArrayList<Unit> vector, ArrayList<ArrayList<Unit>> choices) {

        ArrayList<Unit> choice = new ArrayList<Unit>();

        // Print elements in the vector
        for (Unit elements : vector) {
            choice.add(elements);
        }
        this.choices.add(choice);
    }

    // Utility function to print all root to leaf paths of an Nary Tree
    public void printAllRootToLeafPaths(Node<Unit> root, ArrayList<Unit> vec, ArrayList<ArrayList<Unit>> choices) {

        // Insert current node's data into the vector
        vec.add(root.getData());

        // If current node is a leaf node
        if (root.getChild().isEmpty()) {

            // Print the path
            printPath(vec, choices);

            // Pop the leaf node and return
            vec.remove(vec.size() - 1);
            return;
        }

        // Recur for all children of the current node
        for (int i = 0; i < root.getChild().size(); i++)

            // Recursive Function Call
            printAllRootToLeafPaths(root.getChild().get(i), vec, choices);

        vec.remove(vec.size() - 1);
    }

    // Function to print root to leaf path
    public void printAllRootToLeafPaths(Node<Unit> root) {

        // Stores the root to leaf path
        ArrayList<Unit> vector = new ArrayList<Unit>();

        // Utility function call
        printAllRootToLeafPaths(root, vector, this.choices);
    }

    // Preferences
    // Function to check whether there is any units clashing
    public static boolean isClash(ArrayList<Unit> path) {

        for (int i = 0; i < path.size(); i++) {
            for (int j = i + 1; j < path.size(); j++) {
                if (path.get(i).getDay() != path.get(j).getDay()) {
                    j += 1;
                    continue;
                }
                if (path.get(i).getEndTime() > path.get(j).getStartTime()) {
                    return true;
                }
                if (path.get(i).getStartTime() < path.get(j).getEndTime()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSatisfyDayInterval(int numberOfDays, ArrayList<Unit> path) {

        ArrayList<Integer> days = new ArrayList<Integer>();
        for (int i = 0; i < path.size(); i++) {

            int day = path.get(i).getDay();
            if (day == -1) { // IGNORE THE ROOT
                continue;
            }
            if (!days.contains(day)) {

                days.add(path.get(i).getDay());
            }
        }
        if (days.size() > numberOfDays) {
            return false;
        }
        return true;
    }

    public static boolean isSatisfyTimeInterval(int minutes, ArrayList<Unit> path) {

        for (int i = 0; i < path.size(); i++) {
            for (int j = i + 1; j < path.size(); j++) {
                if (path.get(i).getDay() != path.get(j).getDay()) {
                    j += 1;
                    continue;
                }
                int difference = path.get(i).getEndTime() - path.get(j).getStartTime();
                if (difference < minutes || difference < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isMorning(ArrayList<Unit> path) {

        for (Unit unit : path) {

            if (unit.getStartTime() <= 600 || unit.getEndTime() >= 1200) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAfternoon(ArrayList<Unit> path) {

        for (Unit unit : path) {

            if (unit.getStartTime() <= 1200 || unit.getEndTime() >= 1800) {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<ArrayList<Node<Unit>>> readFromFile(String filePath) {

        try {
            File fileObject = new File(filePath);
            Scanner myReader = new Scanner(fileObject);

            ArrayList<ArrayList<Node<Unit>>> arrLayers = new ArrayList<ArrayList<Node<Unit>>>();
            String checkName = ".";
            int index = 0;
            ArrayList<Node<Unit>> layer = new ArrayList<Node<Unit>>();

            Node<Unit> root = new Node<Unit>(new Unit("ROOT", -1, -1, -1));
            ArrayList<Node<Unit>> firstElement = new ArrayList<Node<Unit>>();
            firstElement.add(root);
            arrLayers.add(firstElement);

            while (myReader.hasNextLine()) {

                String data = myReader.nextLine();
                String[] unitInfo = data.split(";");

                String name = unitInfo[0];
                if (name.contains(checkName)) {
                    index += 1;
                    name += "." + index;

                    // Final data
                    if (!myReader.hasNextLine()) { arrLayers.add(layer); }

                } else {
                    if (layer.size() != 0) { arrLayers.add(layer); }

                    layer = new ArrayList<Node<Unit>>();
                    checkName = name;
                    index = 1;
                    name += "." + index;
                }
                int day = Integer.parseInt(unitInfo[1]);
                int startTime = Integer.parseInt(unitInfo[2]);
                int endTime = Integer.parseInt(unitInfo[3]);

                // Create a Unit object
                Unit unit = new Unit(name, day, startTime, endTime);

                // Create the Node
                Node<Unit> unitNode = new Node<Unit>(unit);
                layer.add(unitNode);
            }
            myReader.close();

            // Add children
            int arrLayersSize = arrLayers.size(); // get the Array of Layers' size
            for (int i = 0; i < arrLayersSize - 1; i++) {

                int layerSize = arrLayers.get(i).size(); // get the current size
                for (int j = 0; j < layerSize; j++) {

                    int nextLayerSize = arrLayers.get(i + 1).size(); // get the next size
                    for (int k = 0; k < nextLayerSize; k++) {

                        arrLayers.get(i).get(j).getChild().add(arrLayers.get(i + 1).get(k));
                    }
                }
            }

            return arrLayers;

        } catch (FileNotFoundException e) {

            System.out.println("File Not Found.");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        // Function Call
        Timetable t = new Timetable();
        ArrayList<ArrayList<Node<Unit>>> data = readFromFile("sampledata2.csv");

        t.printAllRootToLeafPaths(data.get(0).get(0));

        // Preferences
        int numberOfDays = 2;
        int numberOfMinutes = 0;
        boolean wantsMorning = true;
        boolean wantsAfternoon = false;

        System.out.println("No of Days:      " + numberOfDays);
        System.out.println("No of Minutes:   " + numberOfMinutes);
        System.out.println("Wants Morning:   " + wantsMorning);
        System.out.println("Wants Afternoon: " + wantsAfternoon);
        System.out.println();

        // Displaying the result
        int index = 1;
        for (ArrayList<Unit> c : t.choices) {

            if (isClash(c)) {
                continue;
            }
            if (!isSatisfyDayInterval(numberOfDays, c)) {
                continue;
            }
            if (!isSatisfyTimeInterval(numberOfMinutes, c)) {
                continue;
            }
            if (isMorning(c) && wantsMorning) {
                continue;
            }
            if (isAfternoon(c) && wantsAfternoon) {
                continue;
            }

            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            System.out.println("Possible Schedule No. " + index);
            System.out.println("----------------+---------------+---------------+---------------");
            System.out.println("Event\t\t| Day\t\t| Start Time\t| End Time");
            System.out.println("----------------+---------------+---------------+---------------");
            for (Unit c1 : c) {
                if (c1.toString() == "ROOT") {
                    continue;
                }
                System.out.print(c1 + "\t| ");
                System.out.print(String.format("%-14s", days[c1.getDay() - 1]) + "| ");
                System.out.print(String.format("%-14s", c1.getStartTime()));
                System.out.println("| " + c1.getEndTime());
            }
            System.out.println("----------------+---------------+---------------+---------------");
            System.out.println();
            index++;
        }
    }
}

// printPath(), printAllRootToLeafPaths(), printAllRootToLeafPaths() codes are contributed by sanjeev2552