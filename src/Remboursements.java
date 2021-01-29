/**
 * Cette classe retourne un fichier JSON
 * qui contient les informations des soins
 * d'un patient dans un mois.
 *
 * @author Equipe 8
 * @Cours: INF2050-20
 * @version 2021-01-26
 **/
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.google.gson.Gson;

public class Remboursements {
    public static void main(String[] args) throws IOException, ParseException {
        List<String> l1 = CreateListe();
        boolean isValid = valid(l1);
        if (isValid){
            List<String> remboursements = calcul(l1);
            Map<String, String> fileJSON = JSONOutputfile("refunds.json", l1, remboursements);
        } else {
            JSONMessageErreur("Erreur.json");
        }
    }
    /** Permet de lire le fichier inputFile.json
     * et retourner une liste 'list' qui contient
     * toutes les valeurs du fichier (JSONObject et JSONArray)
     *
     * @return Une liste List<String>.
     */
    public static List<String> CreateListe() throws IOException, ParseException {
        List<String> list = new ArrayList<>();
        list1(list);
        list2(list);
        return list;
    }
    public static void list1(List<String> list) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("inputfile.json"));
        JSONObject patient = (JSONObject) obj;
        list.add((String) patient.get("client"));
        list.add((String) patient.get("contrat"));
        list.add((String) patient.get("mois"));
        JSONArray jsonArray = (JSONArray) patient.get("reclamations");
    }
    public static void list2(List<String> list) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("inputfile.json"));
        JSONObject patient = (JSONObject) obj;
        JSONArray jsonArray = (JSONArray) patient.get("reclamations");
        Iterator<JSONObject> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JSONObject rec = iterator.next();
            String soin = String.valueOf(rec.get("soin"));
            list.add(soin);
            list.add((String) rec.get("date"));
            list.add((String) rec.get("montant"));
        }
    }

    /**
     * Permet de retourner une nouvelle liste 'nvList'
     * qui contient les valeurs de remboursement après les calculs,
     * selon le type de contrat et numéro de soin.
     *
     * @return Une liste List<String>.
     */
    public static List<String> calcul(List<String> list) {
        List<String> nvList = new ArrayList<>();
        String contrat = list.get(1);
        if (contrat.equals("A")) {
            nvList = calculContratA(list);
        } else if (contrat.equals("B")) {
            nvList = calculContratB(list);
        } else if (contrat.equals("C")) {
            nvList = calculContratC(list);
        } else if (contrat.equals("D")) {
            nvList = calculContratD(list);
        }
        return nvList;
    }
    /**
     * Permet de retourner une nouvelle liste 'nvList'
     * qui contient les valeurs de remboursement après les calculs,
     * si le contrat est du type "A" selon numéro de soin.
     *
     * @return Une liste List<String>.
     */
    public static List<String> calculContratA(List<String> list){
        List<String> nvList = new ArrayList<>();
        for (int i = 3; i < list.size(); i += 3) {
            int soin = Integer.parseInt(list.get(i));
            float nvMontant = replaceAllMontant(list, i);
            if (soin == 0 || soin == 100 || soin == 200 || soin == 500) {
                nvMontant *= 0.25;
            } else if (soin >= 300 && soin <= 400 || soin == 700) {
                nvMontant = 0;
            } else if (soin == 600) {
                nvMontant *= 0.4;
            }
            String remboursement = String.format("%.2f", nvMontant);
            nvList.add(remboursement);
        }
        return nvList;
    }
    /**
     * Permet de retourner une nouvelle liste 'nvList'
     * qui contient les valeurs de remboursement après les calculs,
     * si le contrat est du type "B" selon numéro de soin.
     *
     * @return Une liste List<String>.
     */
    public static List<String> calculContratB(List<String> list){
        List<String> nvList = new ArrayList<>();
        for (int i = 3; i < list.size(); i += 3) {
            int soin = Integer.parseInt(list.get(i));
            float nvMontant = replaceAllMontant(list, i);
            if (soin == 0) {
                nvMontant *= 0.50;
                if (nvMontant > 40) {
                    nvMontant = 40;
                }
            } else if (soin == 100 || soin == 500) {
                nvMontant *= 0.50;
                if (nvMontant > 50) {
                    nvMontant = 50;
                }
            } else if (soin == 200) {
                nvMontant *= 1;
                if (nvMontant > 70) {
                    nvMontant = 70;
                }
            } else if (soin >= 300 && soin < 400) {
                nvMontant *= 0.5;
            } else if (soin == 400) {
                nvMontant = 0;
            } else if (soin == 600) {
                nvMontant *= 1;
            } else if (soin == 700) {
                nvMontant *= 0.7;
            }
            String remboursement = String.format("%.2f", nvMontant);
            nvList.add(remboursement);
        }
        return nvList;
    }
    /**
     * Permet de retourner une nouvelle liste 'nvList'
     * qui contient les valeurs de remboursement après les calculs,
     * si le contrat est du type "C" selon numéro de soin.
     *
     * @return Une liste List<String>.
     */
    public static List<String> calculContratC(List<String> list){
        List<String> nvList = new ArrayList<>();
        for (int i = 3; i < list.size(); i += 3) {
            int soin = Integer.parseInt(list.get(i));
            float nvMontant = replaceAllMontant(list, i);
            if (soin == 0 || soin == 100 || soin == 200 || soin >= 300 && soin <= 400
                    || soin == 500 || soin == 600 || soin == 700) {
                nvMontant *= 0.90;
            }
            String remboursement = String.format("%.2f", nvMontant);
            nvList.add(remboursement);
        }
        return nvList;
    }
    /**
     * Permet de retourner une nouvelle liste 'nvList'
     * qui contient les valeurs de remboursement après les calculs,
     * si le contrat est du type "D" selon numéro de soin.
     *
     * @return Une liste List<String>.
     */
    public static List<String> calculContratD(List<String> list){
        List<String> nvList = new ArrayList<>();
        for (int i = 3; i < list.size(); i += 3) {
            int soin = Integer.parseInt(list.get(i));
            float nvMontant = replaceAllMontant(list, i);
            if (soin == 0) {
                nvMontant *= 1;
                if (nvMontant > 85) {
                    nvMontant = 85;
                }
            } else if (soin == 100 || soin == 500) {
                nvMontant *= 1;
                if (nvMontant > 75) {
                    nvMontant = 75;
                }
            } else if (soin == 200 || soin == 600) {
                nvMontant *= 1;
                if (nvMontant > 100) {
                    nvMontant = 100;
                }
            } else if (soin >= 300 && soin < 400) {
                nvMontant *= 1;
            } else if (soin == 400) {
                nvMontant *= 1;
                if (nvMontant > 65) {
                    nvMontant = 65;
                }
            } else if (soin == 700) {
                nvMontant *= 1;
                if (nvMontant > 90) {
                    nvMontant = 90;
                }
            }
            String remboursement = String.format("%.2f", nvMontant);
            nvList.add(remboursement);
        }
        return nvList;
    }
    /**
     * permet d'enlever le signe '$' dans les montants
     * et regler le probleme de la virgule ,
     *
     * @return Un float nvMontant.
     */
    public static float replaceAllMontant(List<String> list, int i){
        float nvMontant;
        String montant = list.get(i + 2).replaceAll(".$", "");
        montant = montant.replaceAll(",", ".");
        nvMontant = Float.valueOf(montant);
        return nvMontant;
    }
    /**
     * Permet de vérifier si les donnèes dans le inputFile.JSON
     * sont valides ça va retourner true,
     * au contraire ça va retourner false.
     *
     * @return Un boolean.
     */
    public static boolean valid(List<String> list) {
        if (!validerClient(list) || !validerContrat(list) || !validerTypeSoin(list)
                || !validerDates(list) || !validerMois(list) || !validerMoisMois(list)){
            return false;
        }
        return true;
    }
    /**
     * Permet de vérifier si les donnèes du client
     * sont valides.
     *
     * @return Un boolean.
     */
    public static boolean validerClient(List<String> list){
        boolean isValid = true;
        String client = list.get(0);
        if (client.length() != 6) {
            isValid = false;
        }
        try {
            int d = Integer.parseInt(client);
        } catch (NumberFormatException nfe) {
            isValid = false;
        }
        return isValid;
    }
    /**
     * Permet de vérifier si les donnèes du contrat
     * sont valides.
     *
     * @return Un boolean.
     */
    public static boolean validerContrat(List<String> list){
        boolean isValid = true;
        String contrat = list.get(1);
        if (!contrat.equals("A") && !contrat.equals("B") && !contrat.equals("C") && !contrat.equals("D")){
            isValid = false;
        }
        return isValid;
    }
    /**
     * Permet de vérifier si les tupe du soin
     * sont valides.
     *
     * @return Un boolean.
     */
    public static boolean validerTypeSoin(List<String> list){
        boolean isValid = true;
        for (int i = 3; i < list.size(); i += 3) {
            int soin = Integer.parseInt(list.get(i));
            if (soin < 300 || soin > 399) {
                if (soin != 0 && soin != 100 && soin != 200 && soin != 400 && soin != 500
                        && soin != 600 && soin != 700) {
                    isValid = false;
                }
            }
        }
        return isValid;
    }
    /**
     * Permet de vérifier si les dates
     * sont valides.
     *
     * @return Un boolean.
     */
    public static boolean validerDates(List<String> list){
        boolean isValid = true;
        if (list.get(2).matches("([0-9]{4})-([0-9]{2})")){
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }
    /**
     * Permet de vérifier le format de la date.
     *
     * @return Un boolean.
     */
    public static boolean validerMois(List<String> list) {
        boolean isValid = true;
        for (int i = 4; i < list.size(); i += 3) {
            String dateSoin = list.get(i);
            if (dateSoin.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")) {
                isValid = true;
            } else {
                return false;
            }
        }
        return isValid;
    }
    /**
     * Permet de vérifier le mois de la date
     * est compatibles avec le mois du soin.
     *
     * @return Un boolean.
     */
    public static boolean validerMoisMois(List<String> list){
        boolean isValid = true;
        String mois1 = list.get(2).substring(5, 7);
        for (int i = 4; i < list.size(); i += 3){
            String mois2 = list.get(i).substring(5,7);
            if (mois1.equals(mois2)){
                isValid = true;
            } else {
                isValid = false;
                break;
            }
        }
        return isValid;
    }
    /**
     * Creer un nouveau fichier qui contient le message Données invalides
     * Si les donnèes entreès ne sont pas valides.
     *
     * @return un fichier JSONObject.
     */
    public static void JSONMessageErreur(String filename) {
        JSONObject messageErreur = new JSONObject();
        messageErreur.put("message", "Données invalides");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String erreur = gson.toJson(messageErreur, LinkedHashMap.class);
        try {
            Files.write(Paths.get(filename), erreur.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Creer un nouveau fichier qui contient le résultat final
     * Si les donnèes entreès sont valides.
     *
     * @return un fichier JSONObject.
     */
    public static Map<String, String> JSONOutputfile(String filename, List<String> list, List<String> nvList) {
        List<String> finalList = new ArrayList<String>();
        finalList.add(0, list.get(0));
        finalList.add(1, list.get(2));
        for (int i = 3, j = 0; i < list.size() && j < nvList.size(); i++) {
            if ((i - 2) % 3 == 0) {
                finalList.add(nvList.get(j));
                j++;
            } else {
                finalList.add(list.get(i));
            }
        }
        JSONArray remboursements = new JSONArray();
        for (int i = 2; i < finalList.size(); i += 3){
            LinkedHashMap remboursement = new LinkedHashMap();
            int soin = Integer.parseInt(finalList.get(i));
            remboursement.put("soin", soin);
            remboursement.put("date", finalList.get(i + 1));
            remboursement.put("montant", finalList.get(i + 2) + "$");
            remboursements.add(remboursement);
        }
        LinkedHashMap outputObj = new LinkedHashMap();
        outputObj.put("client", list.get(0));
        outputObj.put("mois", list.get(2));
        outputObj.put("remboursements", remboursements);
        JSONObject object = new JSONObject();
        object.putAll(outputObj);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String refunds = gson.toJson(outputObj, LinkedHashMap.class);
        try {
            Files.write(Paths.get(filename), refunds.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputObj;
    }
}