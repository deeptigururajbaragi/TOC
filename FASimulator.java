/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ddh
 */
public class FASimulator {
    
    public static void main(String[] args) {
//        if (args.length != 3) {
//            System.out.println("Usage: java FASimulator <simulation_type> <nfa_file> <input_file>");
//            return;
//        }

        int simulationType = Integer.parseInt(args[0]);
        String nfaFile = args[1];
        String inputFile = args[2];

        try {
            NFA nfa = new NFA();
            nfa.readFromFile(nfaFile);
            
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String input = br.readLine();
            br.close();

            switch (simulationType) {
                case 1:
                    System.out.println(nfa.simulate(input) ? "yes" : "no");
                    break;
                case 2:
                    DFA dfa = new DFA(nfa);
                    System.out.println(dfa.simulate(input) ? "yes" : "no");
                    break;
                case 3:
                    MinimizedDFA minDfa = new MinimizedDFA(new DFA(nfa));
                    
                    System.out.println(minDfa.simulate(input) ? "yes" : "no");
                    break;
                case 4:
                     List<DFA> dfasForParallel = Arrays.asList(new DFA(nfa));  
                    ParallelDFA pDfa = new ParallelDFA(dfasForParallel);
                    System.out.println(pDfa.simulate(input) ? "yes" : "no");
                    break;
                default:
                    System.out.println("Invalid simulation type");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

