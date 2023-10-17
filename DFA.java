

import java.util.*;

public class DFA {
    private Set<Set<String>> states;  // States of DFA (each state is a set of NFA states)
    private Set<Set<String>> finalStates; // Final states of DFA
    private Set<String> startState;   // Start state of DFA
    private Map<Set<String>, Map<Character, Set<String>>> transitions;  // Transitions of DFA
    private NFA nfa;  // The NFA from which the DFA is derived
    private Set<Character> alphabet;

    // Getter for the alphabet
    public Set<Character> getAlphabet() {
        return alphabet;
    }
    
    
    public DFA(NFA nfa) {
        this.states = new HashSet<>();
        this.finalStates = new HashSet<>();
        this.transitions = new HashMap<>();
        this.nfa = nfa;
        this.alphabet = nfa.getAlphabet();

        convertFromNFA();
    }
    
    public DFA() {
        this.states = new HashSet<>();
        this.finalStates = new HashSet<>();
        this.transitions = new HashMap<>();
    }

    public Set<Set<String>> getStates() {
        return states;
    }

    public void setStates(Set<Set<String>> states) {
        this.states = states;
    }

    public Set<Set<String>> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(Set<Set<String>> finalStates) {
        this.finalStates = finalStates;
    }

    

    public Set<String> getStartState() {
        return startState;
    }

    public void setStartState(Set<String> startState) {
        this.startState = startState;
    }

    public Map<Set<String>, Map<Character, Set<String>>> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<Set<String>, Map<Character, Set<String>>> transitions) {
        this.transitions = transitions;
    }

    public NFA getNfa() {
        return nfa;
    }

    public void setNfa(NFA nfa) {
        this.nfa = nfa;
    }

    private void convertFromNFA() {
    // Initialize DFA start state from the start state of the NFA
    Set<String> initialState = new HashSet<>();
    initialState.add(nfa.getStartState());
    states.add(initialState);
    startState = initialState;

    Queue<Set<String>> queue = new LinkedList<>();
    queue.add(initialState);

    while (!queue.isEmpty()) {
        Set<String> currentState = queue.poll();
        for (char symbol : nfa.getAlphabet()) {
            Set<String> nextState = new HashSet<>();
            for (String state : currentState) {
                if (nfa.getTransitions().containsKey(state) && nfa.getTransitions().get(state).containsKey(symbol)) {
                    nextState.addAll(nfa.getTransitions().get(state).get(symbol));
                }
            }

            if (!states.contains(nextState) && !nextState.isEmpty()) {
                states.add(nextState);
                queue.add(nextState);
            }

            transitions.putIfAbsent(currentState, new HashMap<>());
            transitions.get(currentState).put(symbol, nextState);

            // Check if any state in the nextState set is a final state in the NFA
            if (nextState.stream().anyMatch(nfa.getFinalStates()::contains)) {
                finalStates.add(nextState);  // corrected this line
    }
        }
    }
}


    public boolean simulate(String input) {
    Set<String> currentState = startState;
    for (char symbol : input.toCharArray()) {
        if (transitions.get(currentState) != null && transitions.get(currentState).get(symbol) != null) {
            currentState = transitions.get(currentState).get(symbol);
        } else {
            // No valid transition
            return false;
        }
    }
    return finalStates.contains(currentState);  // corrected this line
}
}
