package ru.nsu.ermakov;

import ru.nsu.ermakov.Table.Alignment;
import ru.nsu.ermakov.Table.Table;
import ru.nsu.ermakov.Text.BoldText;
import ru.nsu.ermakov.Text.Text;

import javax.print.attribute.standard.PrinterMakeAndModel;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String [] args) {
        Element title = new Heading.Builder().lvl(1).value(new Text("Проверка работы Марк Даун генератора")).build();
        Element test = new BoldText("ЖИРНЫЙ ТЕКСТ");
        Element task1 = new Task.Builder().status(true).value(new Text("Написать МаркДаун самому")).build();
        Element task2 = new Task.Builder().status(false).value(new Text ("Апнуть 8к в доте")).build();
        Element taskList = new MarkDownList.Builder().addItem(task1).addItem(task2).build();
        String code = "public static void main(String[] args) {\\n    System.out.println(\\\"Максим Масолыгин красавчик!\\\");\\n} ";
        Element codeblock = new CodeBlock.Builder().language("Java").value(code).build();
        List<Element> tableHeader = Arrays.asList(new Text("Имя"), new Text("Статус"));
        List<Alignment> alignments = Arrays.asList(Alignment.LEFT, Alignment.CENTER);
        List<Element> row1Data = Arrays.asList(new Text("Максим Вылегжанин"), new Text("лох"));
        List<Element> row2Data = Arrays.asList(new Text("Максим Масолыгин"), new Text("красавчик"));
        Element table = new Table.Builder().withHeaderAndAlignments(tableHeader, alignments)
                .addRow(row1Data).addRow(row2Data).build();
        Document finalDocument = new Document.Builder()
                .addElement(title)
                .addElement(test)
                .addElement(taskList)
                .addElement(codeblock)
                .addElement(table)
                .build();
        System.out.println(finalDocument.toMarkDown());
    }
}
