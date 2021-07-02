package ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an IR function as a list of temporary declarations 
 * followed by a list of IR instructions.
 */
public class IRFunction {

    /**
     * Used to construct an IR function.
     */
    public static class Builder {

        private String name = null;
        private IRMethodType type = null;
        private final List<Temporary> temps;
        private final List<IRInstruction> instructions;

        public Builder() {
            temps = new ArrayList<Temporary>();
            instructions = new ArrayList<IRInstruction>();
        }

        public IRFunction build() {
            if (name == null || type == null) {
                throw new IllegalStateException("Must specify a name and type before building an IRFunction");
            }
            return new IRFunction(name, type, temps, instructions);
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withMethodType(IRMethodType type) {
            this.type = type;
            return this;
        }

        public Builder withTemp(Temporary t) {
            temps.add(t);
            return this;
        }

        public Builder withTemps(List<Temporary> ts) {
            temps.addAll(ts);
            return this;
        }

        public Builder addInstruction(IRInstruction instr) {
            instructions.add(instr);
            return this;
        }

        public Builder addInstructions(List<IRInstruction> instrs) {
            instructions.addAll(instrs);
            return this;
        }
    }

    public final String name;
    public final IRMethodType type;
    public final List<Temporary> temps;
    public final List<IRInstruction> instructions;

    public IRFunction(String name, IRMethodType type, List<Temporary> temps, List<IRInstruction> instrs) {
        this.name = name;
        this.type = type;
        this.temps = Collections.unmodifiableList(new ArrayList<Temporary>(temps));
        instructions = Collections.unmodifiableList(new ArrayList<IRInstruction>(instrs));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Add function signature
        sb.append("FUNC ")
          .append(name) 
          .append(' ')
          .append(type.toString())
          .append("\n{\n");

        // Add temporary declarations
        for (var temp : temps) {
            sb.append("\tTEMP")
              .append(temp.globalIndex())
              .append(':')
              .append(temp.type().toIRString())
              .append('\n');
        }

        // Add instructions
        for (var instr : instructions) {
            // TODO don't want to indent labels, but (I think) that's the only exception
            sb.append('\t')
              .append(instr.toString())
              .append('\n');
        }

        sb.append("}");
        return sb.toString();
    }
}