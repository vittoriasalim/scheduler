import java.util.ArrayList;

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

	public static void main(String[] args) {

        Unit empty_unit = new Unit("ROOT", -1, -1, -1);
        Unit u11 = new Unit("COMP2017.1", 1, 1200, 1300);
        Unit u12 = new Unit("COMP2017.2", 2, 1400, 1500);
        Unit u21 = new Unit("INFO2222.1", 2, 1200, 1400);
        Unit u22 = new Unit("INFO2222.2", 4, 1300, 1500);
        Unit u23 = new Unit("INFO2222.3", 1, 1000, 1200);
        Unit u31 = new Unit("SOFT2412.1", 2, 1200, 1400);
        Unit u32 = new Unit("SOFT2412.2", 4, 1300, 1500);

        Node<Unit> root = new Node<Unit>(empty_unit);
        Node<Unit> u11Node = new Node<Unit>(u11);
        Node<Unit> u12Node = new Node<Unit>(u12);
        Node<Unit> u21Node = new Node<Unit>(u21);
        Node<Unit> u22Node = new Node<Unit>(u22);
        Node<Unit> u23Node = new Node<Unit>(u23);
        Node<Unit> u31Node = new Node<Unit>(u31);
        Node<Unit> u32Node = new Node<Unit>(u32);

        root.getChild().add(u11Node);
        root.getChild().add(u12Node);

        // children of u1.1
        u11Node.getChild().add(u21Node);
        u11Node.getChild().add(u22Node);
        u11Node.getChild().add(u23Node);

        // children of u1.2
        u12Node.getChild().add(u21Node);
        u12Node.getChild().add(u22Node);
        u12Node.getChild().add(u23Node);

        // children of u2.1 (from u1.1)
        u21Node.getChild().add(u31Node);
        u21Node.getChild().add(u32Node);

        // children of u2.2 (from u1.1)
        u22Node.getChild().add(u31Node);
        u22Node.getChild().add(u32Node);

        // children of u2.3 (from u1.1)
        u23Node.getChild().add(u31Node);
        u23Node.getChild().add(u32Node);

		// Function Call
		Timetable t = new Timetable();
        t.printAllRootToLeafPaths(root);

        // Preferences
        int numberOfDays = 2;
        int numberOfMinutes = 0;
        boolean wantsMorning = true;
        boolean wantsAfternoon = false;

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

            System.out.println("Possible Schedule No. " + index);
            for (Unit c1 : c) {
                if (c1.toString() == "ROOT") {
                    continue;
                }
                System.out.print(c1 + " ");
            }
            System.out.println("\n");
            index++;
        }
	}
}

// This code is contributed by sanjeev2552