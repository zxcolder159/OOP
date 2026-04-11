package ru.nsu.ermakov;

import ru.nsu.ermakov.Table.Alignment;
import ru.nsu.ermakov.Table.Table;
import ru.nsu.ermakov.Text.BoldText;
import ru.nsu.ermakov.Text.Text;

import javax.print.attribute.standard.PrinterMakeAndModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static ru.nsu.ermakov.Table.Alignment.LEFT;
import static ru.nsu.ermakov.Table.Alignment.RIGHT;

public class Main {
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ФИО\n");
        String FIO = scanner.nextLine();
        Heading title = new Heading.Builder().lvl(1).value(new BoldText(FIO)).build();
        System.out.println("Введите ИНФОРМАЦИЮ О СЕБЕ(email, номер телефона в одну строчку)\n");
        ArrayList<Alignment> Alig = new ArrayList<>();
        Alig.add(LEFT);
        Alig.add(RIGHT);
        ArrayList<Element> HEAD = new ArrayList<>();
        HEAD.add(new Text ("EMAIL"));
        HEAD.add(new Text ("Номер телефона"));
        ArrayList<Element> tbl = new ArrayList<>();
        tbl.add(new Text (scanner.nextLine()));
        tbl.add(new Text (scanner.nextLine()));
        Table table = new Table.Builder().withHeaderAndAlignments(HEAD, Alig).addRow(tbl).build();
        System.out.println("Введите количество своих достижений и затем построчно каждое из них\n");
        int cnt = scanner.nextInt();

        scanner.nextLine();
        MarkDownList.Builder builder = new MarkDownList.Builder();

        for(int i = 0; i < cnt; i++) {
            builder.addItem(new Text(scanner.nextLine()));
        }
        MarkDownList zxc = builder.build();
        Document document = new Document.Builder().addElement(title)
                .addElement(table)
                .addElement(zxc)
                .build();
        File newFile = new File("C:\\Java\\OOP\\Task_1_5_1\\src\\main\\java\\ru\\nsu\\ermakov\\vizit.MD");
        try {
            boolean created = newFile.createNewFile();
            try (FileWriter writer = new FileWriter(newFile)) {
                String markdownContent = document.toMarkDown();
                writer.write(markdownContent);
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(document.toMarkDown());
        scanner.close();
    }
}
