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
public class Main {
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ФИО\n");
        String FIO = scanner.nextLine();
        Heading title = new Heading.Builder().lvl(1).value(new BoldText(FIO)).build();
        System.out.println("Введите ИНФОРМАЦИЮ О СЕБЕ(email, номер телефона в одну строчку)\n");
        String info = scanner.nextLine();
        BoldText infoMD = new BoldText(info);
        System.out.println("Введите количество своих достижений и затем построчно каждое из них\n");
        int cnt = scanner.nextInt();
        ArrayList<Element> achivments = new ArrayList<>();
        scanner.nextLine();
        MarkDownList.Builder builder = new MarkDownList.Builder();

        for(int i = 0; i < cnt; i++) {
            builder.addItem(new Text(scanner.nextLine()));
        }
        MarkDownList zxc = builder.build();
        Document document = new Document.Builder().addElement(title)
                .addElement(infoMD)
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
