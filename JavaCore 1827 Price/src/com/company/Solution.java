package com.company;

/*
1827 Price
CrUD for the table inside the file.
Read the file name for CrUD operations from the console.
The program starts with the following set of parameters:
-c productName price quantity
Parameter Values:
where id is 8 characters.
productName - product name, 30 chars (60 bytes).
price - price, 8 characters.
quantity - quantity, 4 characters.
-c - adds the product with the specified parameters to the end of the file, generates an id independently, incrementing the maximum id found in the file.
The data is stored in the file in the following sequence (without separating spaces):
id productName price quantity
Data is padded with spaces to their length.
Example:
19846 Beach blue shorts 159.00 12
198478 Black beach shorts with a pattern173.00 17
19847983 Jacket for snowboarders, size 10173.991234

Requirements:
1. The program should read the file name for CrUD operations from the console.
2. When starting the program without parameters, the list of products should remain unchanged.
3. When starting the program with the parameters "-c productName price quantity", a new line with the product should be added to the end of the file.
4. The product must have the following id, after the maximum found in the file.
5. The formatting of a new line of goods should clearly coincide with that specified in the assignment.
6. Streams created for files should be closed.


 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Валидацию прошел с 35 попытки. Без малого это п-ц.
//А все потому, что нигде не сказано, что режим append для записи в файл использовать нельзя.
//Зато выскакивают бестолковые подсказки о том что форматирование должно совпадать с условием задачи....
//Решение прошло только после полной перезаписи файла, без append.
//И да, половину проверок скорее всего можно не делать, это перестраховка.

public class Solution {

    public static void main(String[] args) throws Exception {
        if (args.length < 4 || !args[0].equals("-c")) return;
        float price;
        int qty;

        //Если что-то передали не то
        try {
            price = Float.parseFloat(args[args.length - 2]);
            qty = Integer.parseInt(args[args.length - 1]);
        } catch (NumberFormatException e) {
            return;
        }

        //Если у нас аргументов больше 4 из-за пробелов в строке productName
        String productName;
        if (args.length > 4) {
            StringBuffer buf = new StringBuffer();
            for (int i = 1; i < args.length - 2; i++)
                buf.append(args[i]).append(" ");
            productName = buf.substring(0, buf.length() - 1);
        } else
            productName = args[1];

        //Read file name from console
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = consoleReader.readLine();
        consoleReader.close();

        //Get Lines from file
        //List<String> lines = Files.readAllLines(Paths.get(fileName)); //так проще, но не пропускается
        List<String> lines = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            while (fileReader.ready())
                lines.add(fileReader.readLine());
        }

        //Get ID from line
        Pattern p = Pattern.compile("([0-9]{1,8})");
        int maxID = 0;
        for (String s : lines) {
            Matcher m = p.matcher(s);
            if (m.lookingAt()) {
                try {
                    //System.out.println(s.substring(m.start(), m.end()));
                    int id = Integer.parseInt(s.substring(m.start(), m.end()));
                    if (id > maxID)
                        maxID = id;
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        if (maxID++ == 99999999)
            return;
        String toFile = String.format(Locale.ROOT,"%-8d%-30s%-8.2f%-4d", maxID, productName, price, qty);

        lines.add(toFile);
        try (BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))) {
            for (String s: lines)
                buf.write(s+"\r\n");
        }
    }
}

