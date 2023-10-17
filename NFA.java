/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.*;
import java.util.*;

public class NFA {
    private Set<String> states;
    private Set<String> finalStates;
    private String startState;
    private Map<String, Map<Character, List<String>>> transitions;

    public NFA() {
        this.states = new HashSet<>();
        this.finalStates = new HashSet<>();
        this.transitions = new HashMap<>();
    }

    public Set<String> getStates() {
        return states;
    }

    public void setStates(Set<String> states) {
        this.states = states;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(Set<String> finalStates) {
        this.finalStates = finalStates;
    }

    public String getStartState() {
        return startState;
    }

    public void setStartState(String startState) {
        this.startState = startState;
    }

    public Map<String, Map<Character, List<String>>> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<String, Map<Character, List<String>>> transitions) {
        this.transitions = transitions;
    }

    public Set<Character> getAlphabet() {
        Set<Character> alphabet = new HashSet<>();
        for (Map<Character, List<String>> map : transitions.values()) {
            alphabet.addAll(map.keySet());
        }
        return alphabet;
    }

    public void readFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        // Reading final states
        String line = br.readLine();
        for (String state : line.split("\\s+")) {
            finalStates.add(state);
        }
        // Reading start state
        startState = br.readLine().trim();
        // Reading other states
        line = br.readLine();
        for (String state : line.split("\\s+")) {
            states.add(state);
        }
        // Combining all states
        states.addAll(finalStates);
        states.add(startState);
        // Reading transitions
        line = br.readLine();
        for (String transition : line.split("\\s+")) {
            String fromState = Character.toString(transition.charAt(0));
            char symbol = transition.charAt(1);
            String toState = Character.toString(transition.charAt(2));
            transitions.putIfAbsent(fromState, new HashMap<>());
            transitions.get(fromState).putIfAbsent(symbol, new ArrayList<>());
            transitions.get(fromState).get(symbol).add(toState);
        }
        br.close();
    }

    public boolean simulate(String input) {
        Set<String> currentStates = new HashSet<>();
        currentStates.add(startState);

        for (char symbol : input.toCharArray()) {
            // Handle epsilon transitions and update currentStates
            currentStates = processTransitions(currentStates, symbol);
        }

        // Check if any final state is reached
        for (String state : currentStates) {
            if (finalStates.contains(state)) {
                return true;
            }
        }

        return false;
    }

    private Set<String> processTransitions(Set<String> states, char symbol) {
        Set<String> nextStates = new HashSet<>();

        for (String state : states) {
            if (transitions.containsKey(state) && transitions.get(state).containsKey(symbol)) {
                // Follow transitions for the symbol
                nextStates.addAll(transitions.get(state).get(symbol));
            }

            // Handle epsilon transitions
            if (transitions.containsKey(state) && transitions.get(state).containsKey('ε')) {
                nextStates.addAll(transitions.get(state).get('ε'));
            }
        }

        return nextStates;
    }
}