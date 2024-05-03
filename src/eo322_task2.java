// -----------------------------------------------------------------------------
// Author: Elliot Ofesi
// Module: COMP5180
// 	Assignment 2 (2021-22)
// 	Task 2 (dummy program)
// Program:
// Check the correctness of a solution for the Tower of Hanoi problem
// -----------------------------------------------------------------------------

import java.util.*; // Import the util library
import java.io.*; // Import the I/O library

public class eo322_task2 {

    // ---------------------------------------------------------------------
    // Function: Empty Constructor
    // ---------------------------------------------------------------------
    public eo322_task2 ()
    {
    }

    // ---------------------------------------------------------------------
    // Function: isBlank
    // ---------------------------------------------------------------------
    public static boolean isBlank (int character) {
        if (
                character == ' ' ||
                        character == '\t' ||
                        character == '\n' ||
                        character == '\r'
        )
            return true;
        return false;

    }

    // ---------------------------------------------------------------------
    // Function: getNextInteger
    // This function only works assuming that the file has positive integers
    // ---------------------------------------------------------------------
    public static int getNextInteger (FileInputStream input_file) {
        int character;
        int digit;
        int number = 0;
        try {
            while ((character = input_file.read()) != -1 && !isBlank(character))
            {
                number *= 10;
                digit = (int) character - '0';
                number += digit;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }

    // ---------------------------------------------------------------------
    // Function: main
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        int n, t, s, d; //When its only 3 inputs, its the n, s, d. Note that the s and D will dynamically change so its important we remember the inital ones.

        //When its 3 digits, 1st digit represents What Integer u want to move, 2nd digit represents where it currently is, 3rd digit represents where we are moving to.
        String str_filename;
        String my_ID = new String("sb2213");

        // Check if the input filename has been provided as an argument
        if (args.length < 1)
        {
            System.out.printf("Usage: java %s_task2 <file_name>\n", my_ID);
            return;
        }

        try {
            // Get the filename
            str_filename = args[0];
            System.out.printf("Reading the file " + str_filename + "\n");

            // Create the object for reading the input file
            FileInputStream input_file = new FileInputStream(str_filename);

            // Read the four parameters in the first row of the input file
            n = getNextInteger (input_file);
            t = getNextInteger (input_file);
            s = getNextInteger (input_file);
            d = getNextInteger (input_file);
            System.out.printf("%d\t%d\t%d\t%d\n", n, t, s, d);

            ArrayList<Stack> towerList = new ArrayList<Stack>(); //ArrayList for all the towers
            Stack<Integer> sTower= new Stack<Integer>(); //Initialising Source tower since this is guaranteed to be included.
            Stack<Integer> dTower= new Stack<Integer>(); //Initialising Destination tower since this is guaranteed to be included.
            for(int i=1; i<=t;
                i++) //Creation of all the buffer towers. We make equal to 1 cuz we are considering towers "1" to towers "t"
            //we do minus 3 because of the "n-(1+2)" since the n-1 convention .
            {
                if(!(i==s || i==d)) //If it is true that i is not an index for source or destination tower.
                { //Implementing a bufferTower
                    Stack<Integer> tower = new Stack<Integer>();
                    towerList.add(tower);
                    System.out.println("Implementing buffer tower: " + i);
                } else if (i == s) //Implementing Source tower
                {
                    System.out.println("Implementing source tower: " + i + " equals to " + s);
                    for (int j=0; j<n; j++) //implementing starting Integer into source tower.
                    {
                        Integer a = n - j; //Use this equation to implement to push the discs in the right order
                        sTower.push(a);
                    }
                    towerList.add(sTower);
                } else if (i == d) //Implementing Destination Tower to list
                {
                    System.out.println("Implementing destination tower: " + i + " equals to " + d); // if it does not equal to then there is an error
                    towerList.add(dTower);
                }

            }

            System.out.println(towerList);
            while(input_file.available() > 0) {
                t = getNextInteger(input_file); //
                s = getNextInteger(input_file);
                d = getNextInteger(input_file);
                System.out.printf("%d\t%d\t%d\n", t, s, d);

                if (towerList.get(d - 1).isEmpty()) {
                    {
                        if ((Integer) towerList.get(s - 1).peek() == t) { //True if the tower disc is equal to the tower that was requested to ve moved
                            towerList.get(d - 1).push(towerList.get(s - 1).pop()); //pop disc from source tower and push into destination tower.
                            System.out.println(towerList);
                        } else {
                            //System.out.print("disc either not in source tower or an illegal move was made.");
                            System.out.println(towerList);
                            throw new IOException("disc either not in source tower or an illegal move was made.");
                        }
                    }


                } else { //Same functionality as above
                    if ((Integer) towerList.get(s - 1).peek() == t) //used casting since peek return objects and not integers
                    // true if the disc we’re moving is equivalent to the disc from source location S
                    {
                        if((Integer) towerList.get(s - 1).peek() < (Integer) towerList.get(d - 1).peek())
                        // true if source disc is smaller than destination disc making this a legal move. Or, if the disc is empty.
                        {
                            towerList.get(d - 1).push(towerList.get(s - 1).pop()); //pop disc from source tower and push into destination tower.
                            System.out.println(towerList);
                        } else
                        {
                            //System.out.print("disc being moved is smaller than the disc present at the top of the destination tower");
                            System.out.println(towerList);
                            throw new IOException("disc being moved is smaller than the disc present at the top of the destination tower");
                        }

                    } else {
                        //System.out.print("Disc being moved is not at the top of the source tower.");
                        System.out.println(towerList);
                        throw new IOException("Disc being moved is not at the top of the source tower.");

                    }

                }
            }

            //Requirement 3 Error checking
            for (int k=0; k<=towerList.size()-1; k++)
            {
                if (k == d-1) //true if current index is final destination tower
                { //So if this is true then we will check the destination disc to see if all the discs are present in the right location.
                    if (!(towerList.get(k).size() == n)) //true if all the current discs is not equal to file input current disc
                    {
                        //System.out.println("Error, all discs have not been placed in destination disc");
                        throw new IOException("Error, all discs have not been placed in destination disc");
                    }
                } else
                {
                    if (!(towerList.get(k).isEmpty())) //True if this tower is not empty meaning there is an error.
                    {
                        //System.out.println("Error, all discs have not been placed in destination disc");
                        throw new IOException("Error, all discs have not been placed in destination disc");
                    }
                }
            }
            // Close the file
            input_file.close();



        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("\n");
        return;
    }


}