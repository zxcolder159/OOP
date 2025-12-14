package ru.nsu.ermakov.Table;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import ru.nsu.ermakov.Element;

public class Table implements Element {
    private final List<Alignment> alignments;
    private final List<Element> header;
    private final List<List<Element>> rows;
    private Table(Builder builder){
        this.alignments = builder.alignments;
        this.header = builder.header;
        this.rows = builder.rows;
    }

    @Override
    public String toMarkDown() {
        if (header.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("|");
        for (Element headerCell : header) {
            String content = headerCell.toMarkDown().replace("\n", "");
            sb.append(" ").append(content).append(" |");
        }
        sb.append("\n");


        sb.append("|");
        for (Alignment alignment : alignments) {
            String alignmentCode = alignment.getMarkdownCode();
            sb.append(" ").append(alignmentCode).append(" |");
        }
        sb.append("\n");


        for (List<Element> row : rows) {
            sb.append("|");
            for (Element cell : row) {
                String content = cell.toMarkDown().replace("\n", "");
                sb.append(" ").append(content).append(" |");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        // Сравниваем все три списка
        return Objects.equals(alignments, table.alignments) &&
                Objects.equals(header, table.header) &&
                Objects.equals(rows, table.rows);
    }
    @Override
    public int hashCode() {
        return Objects.hash(alignments, header, rows);
    }

    public static class Builder {
        private List<Alignment> alignments = null;
        private List<Element> header = null;
        private List<List<Element>> rows = new ArrayList<>();
        public Builder withHeaderAndAlignments(List<Element> header, List<Alignment> alignments) {
            if (header == null || alignments == null) {
                throw new IllegalStateException("Header and Alignments must not be null.");
            }
            if (header.size() != alignments.size()) {
                throw new IllegalStateException("Header size must match Alignment size.");
            }
            this.header = header;
            this.alignments = alignments;
            return this;
        }
        public Builder addRow(List<Element> rowElements) {
            if (this.header == null) {
                throw new IllegalStateException("Header must be set before adding rows.");
            }
            if (rowElements.size() != this.header.size()) {
                throw new IllegalStateException("Row size must match header size (" + this.header.size() + ").");
            }
            this.rows.add(rowElements);
            return this;
        }
        public Table build() {
            if (this.header == null) {
                throw new IllegalStateException("Table cannot be built without header and alignments.");
            }
            if (this.header.isEmpty()) {
                throw new IllegalStateException("Table must have at least one column.");
            }

            return new Table(this);
        }
    }
}
