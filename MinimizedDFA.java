

import java.util.*;

public class MinimizedDFA {
    private DFA dfa;
    private Set<Set<String>> partitions;
    private Map<String, Set<String>> stateToPartition;
    private DFA minimizedDfa;

    public MinimizedDFA(DFA dfa) {
        this.dfa = dfa;
        this.partitions = new HashSet<>();
        this.stateToPartition = new HashMap<>();
        minimize();
    }

    public DFA getMinimizedDFA() {
        return minimizedDfa;
    }

    private void minimize() {
        // Initialize partitions with final and non-final states
        partitions.addAll(dfa.getFinalStates());
        Set<String> nonFinalStates = new HashSet<>();
        for (Set<String> stateSet : dfa.getStates()) {
            nonFinalStates.addAll(stateSet);
        }
        nonFinalStates.removeAll(dfa.getFinalStates());
        partitions.add(nonFinalStates);

        // Mapping from state to its partition
        for (Set<String> partition : partitions) {
            for (String state : partition) {
                stateToPartition.put(state, partition);
            }
        }

        boolean changed;
        do {
            changed = false;
            Set<Set<String>> newPartitions = new HashSet<>();
            for (Set<String> partition : partitions) {
                Map<Set<String>, Set<String>> signatureToStates = new HashMap<>();
                for (String state : partition) {
                    Set<String> signature = new HashSet<>();
                    for (char symbol : dfa.getAlphabet()) {
                        Map<Character, Set<String>> stateTransitions = dfa.getTransitions().get(state);
                        Set<String> targetState = null;

                        if (stateTransitions != null) {
                            targetState = stateTransitions.get(symbol);
                        }

                        if (targetState != null) {
                            signature.add(String.join(",", stateToPartition.get(targetState)));
                        }
                    }
                    signatureToStates.putIfAbsent(signature, new HashSet<>());
                    signatureToStates.get(signature).add(state);
                }
                newPartitions.addAll(signatureToStates.values());
            }
            if (newPartitions.size() != partitions.size()) {
                changed = true;
                partitions = newPartitions;
            }
        } while (changed);

        // Construct minimized DFA from partitions
        Set<Set<String>> minimizedStates = new HashSet<>(partitions);
        Map<Set<String>, Map<Character, Set<String>>> minimizedTransitions = new HashMap<>();
        for (Set<String> stateSet : minimizedStates) {
            for (char symbol : dfa.getAlphabet()) {
                String representativeState = stateSet.iterator().next();
                Map<Character, Set<String>> stateTransitions = dfa.getTransitions().get(symbol);
                        Set<String> targetState = null;

                        if (stateTransitions != null) {
                            targetState = stateTransitions.get(symbol);
                        }
                if (targetState != null) {
                    minimizedTransitions.put(stateSet, new HashMap<>());
                    minimizedTransitions.get(stateSet).put(symbol, stateToPartition.get(targetState));
                }
            }
        }
        Set<String> minimizedStartState = stateToPartition.get(dfa.getStartState());

        // Construct the minimized DFA object
        minimizedDfa = new DFA();
        minimizedDfa.setStates(minimizedStates);
        minimizedDfa.setTransitions(minimizedTransitions);
        minimizedDfa.setStartState(minimizedStartState);
    }
    public boolean simulate(String input) {
    Set<String> currentState = minimizedDfa.getStartState();
    for (char symbol : input.toCharArray()) {
        if (minimizedDfa.getTransitions().get(currentState) != null && minimizedDfa.getTransitions().get(currentState).get(symbol) != null) {
            currentState = minimizedDfa.getTransitions().get(currentState).get(symbol);
        } else {
            // No valid transition
            return false;
        }
    }
    return minimizedDfa.getFinalStates().contains(currentState);
}

}
