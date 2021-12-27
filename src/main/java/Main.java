import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Document document = Jsoup.connect("https://patronite.pl/Baniakbaniaka").get();
        Elements patroni = document.getElementsByClass("box--footer");
        Elements kwoty = document.getElementsByClass("author__stats--number");

        List<Integer> cenaList = createCenaList(kwoty);
        List<Integer> patroniList = createPatroniList(patroni);


        komunitakPierwszy(cenaList);
        Integer pelnaKwota = przeliczKwoty(patroniList, cenaList);
        komunitakDrugi(pelnaKwota);

        saveFile(pelnaKwota, aktualnaData());
    }

    private static void komunitakPierwszy(List<Integer> cenaList) {
        System.out.println();
        System.out.println("Patroni Baniaka (" + cenaList.get(0) + "):");
        System.out.println();
    }

    private static void komunitakDrugi(Integer pelnaKwota) {
        System.out.println();
        System.out.println("Suma: " + pelnaKwota + " zł");
    }


    private static List<Integer> createPatroniList(Elements patroni) {
        List<Integer> lista = new ArrayList<>();

        for (int i = 0; i < patroni.toArray().length; i++) {
            String[] split = patroni.get(i).text().split(" ");
            if (split.length > 1) {
                Integer patroniProgu = Integer.parseInt(split[1]);
                lista.add(patroniProgu);
            }
        }

        return lista;
    }

    private static List<Integer> createCenaList(Elements kwoty) {
        List<Integer> lista = new ArrayList<>();

        for (int i = 0; i < kwoty.toArray().length; i++) {
            Integer cena = Integer.parseInt(kwoty.get(i).text().split(" ")[0]);
            lista.add(cena);
        }

        return lista;
    }

    private static Integer przeliczKwoty(List<Integer> patroniList, List<Integer> cenaList) {
        Integer pelnaKwota = 0;
        for (int i = 0; i < patroniList.toArray().length; i++) {
            Integer patron = patroniList.get(i);
            Integer cena = cenaList.get(i + 1);
            Integer suma = patron * cena;

            pelnaKwota += suma;
            System.out.println("Patroni(" + cena + "zł): " + patron + " * " + cena + " zł = " + suma + " zł");
        }

        return pelnaKwota;
    }

    private static void saveFile(Integer pelnaKwota, String aktualnaData) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Czy zapisać dane? (y/n)");
        String odp = scanner.nextLine();

        if (odp.equals("y")) {
            String data = "";
            File myObj = new File("pratronite.csv");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + "\n";
            }

            FileWriter fileWriter = new FileWriter("pratronite.csv");
            fileWriter.write(data);
            fileWriter.write(aktualnaData + "," + pelnaKwota + ",PLN");
            fileWriter.close();

            System.out.println("Zapisano");
        } else if (odp.equals("n")) {
            return;
        } else {
            System.out.println("Nie zapisano i opuszczono aplikacje");
        }
    }

    private static String aktualnaData() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}
