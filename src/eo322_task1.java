// -----------------------------------------------------------------------------
// Author: Elliot Ofesi, Sanjay Bhattacherjee
// Module: COMP5180
// 	Assignment 2 (2021-22)
// 	Task 1 (eo322_Task)
// Problem:
// Tower of Hanoi problem with (3) of towers
// -----------------------------------------------------------------------------

import java.util.Scanner;  // Import the Scanner class
import java.io.*; // Import the I/O library
import java.util.ArrayList;
public class eo322_task1 {

    // ---------------------------------------------------------------------
    // Function: Empty Constructor BUT NO LONGER!
    // ---------------------------------------------------------------------
    public eo322_task1 ()
    {
    }

    // ---------------------------------------------------------------------
    // Function: Prints every move on screen and also writes it to a file
    // ---------------------------------------------------------------------
    public int print_move (int disc, int source_tower, int destination_tower, FileWriter writer)
    {
        try {
            System.out.printf("\nMove disk %d from T%d to T%d", disc, source_tower, destination_tower);
            writer.write("\n" + disc + "\t" +  source_tower + "\t" + destination_tower);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ---------------------------------------------------------------------
    // Function: The recursive function for moving n discs
    //           from s to d with t-2 buffer towers.
    //           It prints all the moves with disk numbers.
    // ---------------------------------------------------------------------

    public void oddTransition(int T, int S, int D, int B, FileWriter writer)
    //This will stack 3 discs onto the destination tower
    {
        //T-2 representing the smallest of the 3 and T representing the biggest
        //System.out.println("");
        //System.out.println("Odd transition here");
        //System.out.println("Source tower: " + S + " Buffer tower: " + B + " Destination tower: " + D);
        print_move(T-2, S, D, writer);
        print_move(T-1, S, B, writer);
        print_move(T-2, D, B, writer);
        print_move(T, S, D, writer);
        print_move(T-2, B, S, writer);
        print_move(T-1, B, D, writer);
        print_move(T-2, S, D, writer);


    }

    public void evenTransition(int T, int S, int D, int B, FileWriter writer)
    //This will stack 3 discs into the buffer tower
    {
        //System.out.println("");
       //System.out.println("Even transition here");
        //System.out.println("Source tower: " + S + " Buffer tower: " + B + " Destination tower: " + D);
        print_move(T-2, S, B, writer);
        print_move(T-1, S, D, writer);
        print_move(T-2, B, D, writer);
        print_move(T, S, B, writer);
        print_move(T-2, D, S, writer);
        print_move(T-1, D, B, writer);
        print_move(T-2, S, B, writer);
    }

    public int[] move_t (int number_of_discs, int number_of_towers, int source_tower, int destination_tower, int buffer_Tower, boolean saveValues, FileWriter writer) //bufferTower will be an arraylist instead when we write recursive
    {
        //Switch with all possible cases depending on the number of discs.
        switch(number_of_discs)
        {
            case 1: //If there are 1 discs
                print_move(1, source_tower, destination_tower, writer);
                break;

            case 2: //If there are 2 discs
                print_move(1, source_tower, buffer_Tower, writer);
                print_move(2, source_tower, destination_tower, writer);
                print_move(1, buffer_Tower, destination_tower, writer);
                break;

            case 3: //If there are 3 discs
                oddTransition(number_of_towers, source_tower, destination_tower, buffer_Tower, writer);
                break;

            case 4: //If there are 4 discs
                evenTransition(number_of_discs-1, source_tower, destination_tower, buffer_Tower, writer);
                print_move(number_of_discs, source_tower, destination_tower, writer); //Moving last disc to destination num of disc == last dics
                oddTransition(number_of_discs-1, buffer_Tower, destination_tower, source_tower, writer);
                break;

            default:

                eo322_task1 recursiveToH = new eo322_task1(); //Recursively call ToH to perform tasks
                //Here we're doing the first task of moving stuff into the buffer tower
                int savedValues[] = recursiveToH.move_t(number_of_discs-1, 3, source_tower, buffer_Tower, destination_tower, true, writer);

                //Then we move the last tower into position
                print_move(number_of_discs, source_tower, destination_tower, writer);

                recursiveToH.move_t(number_of_discs-1, 3, savedValues[0], destination_tower, savedValues[1], false, writer);
                //Then we move every disc back into desired position.
        }

        if (saveValues == true)
        {
            int storeValues [] = {destination_tower, source_tower};
            return storeValues;
        }
        return null;
    }

    // ---------------------------------------------------------------------
    // Function: main
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        int n, t, s, d;
        String my_ID = new String("sb2213");

        if (args.length < 4)
        {
            System.out.printf("Usage: java %s_task1 <n> <t> <s> <d>\n", my_ID);
            return;
        }

        n = Integer.parseInt(args[0]);  // Read user input n (How much disc)
        t = Integer.parseInt(args[1]);  // Read user input t (How much towers)
        s = Integer.parseInt(args[2]);  // Read user input s (Source tower)
        d = Integer.parseInt(args[3]);  // Read user input d (Destination tower)



        // Check the inputs for sanity
        if (n<1 || t<3 || s<1 || s>t || d<1 || d>t)
        {
            System.out.printf("Please enter proper parameters. (n>=1; t>=3; 1<=s<=t; 1<=d<=t)\n");
            return;
        }

        // Create the output file name
        String filename;
        filename = new String(my_ID + "_ToH_n" + n + "_t" + t + "_s" + s + "_d" + d + ".txt");
        try {
            // Create the Writer object for writing to "filename"
            FileWriter writer = new FileWriter(filename, true);

            // Write the first line: n, t, s, d
            writer.write(n + "\t" + t + "\t" + s + "\t" + d);

            // Create the object of this class to solve the generalised ToH problem
            eo322_task1 ToH = new eo322_task1();
            System.out.printf("\nThe output is also written to the file %s", filename);

            // Create bufferTower number

            int b=0; //Finding a buffer tower
            for(int i=1; i<=t; i++)
            {
                if ( i != s && i != d)
                {
                    b = i;
                }
                // If u want it to be recursive then just add the free ones into an ArraylisT/Stack of B[]s
            }

            // Call the recursive function that solves the ToH problem
            ToH.move_t(n, t, s, d, b, false, writer); //bufferTower will be an arraylist instead when we write recursive

            // Close the file
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("\n");
        return;
    }
}
