package RefundPackage;

import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

import com.google.gson.Gson;

public class Refund {
    public static void main(String[] args) throws IOException, ParseException {
        List<String> list = CreateListe();
        boolean isValid = valid(list);

        if (isValid) {
            List<String> refunds = calcul(list);
            Map<String, String> fileJSON = JSONOutputfile("refunds.json", list, refunds);
        } else {
            JSONMessageError("Erreur.json");
        }
    }

    /**
     * Permet de lire le fichier inputFile.json et retourner une liste 'list'
     * qui contient toutes les valeurs du fichier (JSONObject et JSONArray)
     *
     * @return Une liste List<String>.
     * @throws IOException la classe de base pour les exceptions lancées lors de l'accès aux informations à l'aide de fichiers et de répertoires.
     * @throws ParseException Signale qu'une erreur a été atteinte de manière inattendue lors de l'analyse.
     */
    public static List<String> CreateListe() throws IOException, ParseException {
        List<String> list = new ArrayList<>();
        CreateObjectFromJSON(list);
        CreateArrayFromJSON(list);
        return list;
    }

    /**
     * Permet de lire le fichier inputFile.json
     *
     * @return un JSONObject patient.
     * @throws IOException la classe de base pour les exceptions lancées lors de l'accès aux informations à l'aide de fichiers et de répertoires.
     * @throws ParseException Signale qu'une erreur a été atteinte de manière inattendue lors de l'analyse.
     */
    public static JSONObject ReadFile() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader("inputfile.json"));
        JSONObject patient = (JSONObject) object;
        return patient;
    }

    /**
     * Permet d'ajouter les valeurs JSONObject dans List<String>
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @throws IOException la classe de base pour les exceptions lancées lors de l'accès aux informations à l'aide de fichiers et de répertoires.
     * @throws ParseException Signale qu'une erreur a été atteinte de manière inattendue lors de l'analyse.
     */
    public static void CreateObjectFromJSON(List<String> list) throws IOException, ParseException {
        JSONObject patient = ReadFile();

        list.add((String) patient.get("client"));
        list.add((String) patient.get("contrat"));
        list.add((String) patient.get("mois"));

        //JSONArray jsonArray = (JSONArray) patient.get("reclamations");
    }

    /**
     * Permet d'ajouter les valeurs JSONArray dans List<String>
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @throws IOException la classe de base pour les exceptions lancées lors de l'accès aux informations à l'aide de fichiers et de répertoires.
     * @throws ParseException Signale qu'une erreur a été atteinte de manière inattendue lors de l'analyse.
     */
    public static void CreateArrayFromJSON(List<String> list) throws IOException, ParseException {
        JSONObject patient = ReadFile();
        JSONArray jsonArray = (JSONArray) patient.get("reclamations");
        Iterator<JSONObject> iterator = jsonArray.iterator();

        if (!iterator.hasNext()) {
            JSONMessageError("Erreur.json");
            System.exit(1);
        }
        while (iterator.hasNext()) {
            JSONObject saveData = iterator.next();
            String care = String.valueOf(saveData.get("soin"));
            list.add(care);
            list.add((String) saveData.get("date"));
            list.add((String) saveData.get("montant"));
        }
    }

    /**
     * Permet de retourner une nouvelle liste 'newList'
     * qui contient les valeurs de remboursement après les calculs,
     * selon le type de contrat et numéro de soin.
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Une liste List<String>
     */
    public static List<String> calcul(List<String> list) {
        List<String> newList = new ArrayList<>();
        String contract = list.get(1);

        if (contract.equals("A")) newList = calculcontractA(list);
        else if (contract.equals("B")) newList = calculcontractB(list);
        else if (contract.equals("C")) newList = calculcontractC(list);
        else if (contract.equals("D")) newList = calculcontractD(list);

        return newList;
    }

    /**
     * Permet de retourner une nouvelle liste 'newList'
     * qui contient les valeurs de remboursement après les calculs,
     * si le contrat est du type "A" selon numéro de soin.
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Une liste List<String>.
     */
    public static List<String> calculcontractA(List<String> list) {
        List<String> newList = new ArrayList<>();

        for (int i = 3; i < list.size(); i += 3) {
            int care = Integer.parseInt(list.get(i));
            float newPrice = replaceAllPrices(list, i);

            if (care == 0 || care == 100 || care == 200 || care == 500) newPrice *= 0.25;
            else if (care >= 300 && care <= 400 || care == 700) newPrice = 0;
            else if (care == 600) newPrice *= 0.4;

            String refund = String.format("%.2f", newPrice);

            newList.add(refund);
        }
        return newList;
    }

    /**
     * Permet de retourner une nouvelle liste 'newList' qui contient les
     * valeurs de remboursement après les calculs, si le contrat est du type
     * "B" selon numéro de soin.
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Une liste List<String>.
     */
    public static List<String> calculcontractB(List<String> list) {
        List<String> newList = new ArrayList<>();
        for (int i = 3; i < list.size(); i += 3) {
            int care = Integer.parseInt(list.get(i));
            float newPrice = replaceAllPrices(list, i);

            if (care == 0 || care == 100 || care == 500) { newPrice *= 0.50;

                if (care == 0 && newPrice > 40) newPrice = 40;
                if (care == 100 || care == 500 && newPrice > 50) newPrice = 50;
            } else if (care == 200 || care == 600) { newPrice *= 1;

                if (care == 200 && newPrice > 70) newPrice = 70;
            } else if (care >= 300 && care < 400) newPrice *= 0.5;
            else if (care == 400) newPrice = 0;
            else if (care == 700) newPrice *= 0.7;

            String refund = String.format("%.2f", newPrice);
            newList.add(refund);
        }
        return newList;
    }

    /**
     * Permet de retourner une nouvelle liste 'newList'
     * qui contient les valeurs de remboursement après les calculs,
     * si le contrat est du type "C" selon numéro de soin.
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Une liste List<String>.
     */
    public static List<String> calculcontractC(List<String> list) {
        List<String> newList = new ArrayList<>();

        for (int i = 3; i < list.size(); i += 3) {
            int care = Integer.parseInt(list.get(i));
            float newPrice = replaceAllPrices(list, i);

            if (care == 0 || care == 100 || care == 200 || care >= 300 && care <= 400 || care == 500 || care == 600 || care == 700)
                newPrice *= 0.90;
            String refund = String.format("%.2f", newPrice);
            newList.add(refund);
        }
        return newList;
    }

    /**
     * Permet de retourner une nouvelle liste 'newList'
     * qui contient les valeurs de remboursement après les calculs,
     * si le contrat est du type "D" selon numéro de soin.
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Une liste List<String>.
     */
    public static List<String> calculcontractD(List<String> list) {
        List<String> newList = new ArrayList<>();

        for (int i = 3; i < list.size(); i += 3) {
            int care = Integer.parseInt(list.get(i));
            float newPrice = replaceAllPrices(list, i);

            if (care == 0 || care == 100 || care == 500 || care == 200 || care == 600 || care >= 300 && care < 400
                    || care == 400 || care == 700) newPrice *= 1;
                if (care == 0 && newPrice > 85) newPrice = 85;
                if (care == 100 || care == 500 && newPrice > 75) newPrice = 75;
                if (care == 200 || care == 600 && newPrice > 100) newPrice = 100;
                if (care == 400 && newPrice > 65) newPrice = 65;
                if (care == 700 && newPrice > 90) newPrice = 90;

            String refund = String.format("%.2f", newPrice);
            newList.add(refund);
        }
        return newList;
    }

    /**
     * permet d'enlever le signe '$' dans les montants et regler le probleme
     * de la virgule
     *
     * @param  list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @param i un compteur.
     * @return Un float nvMontant.
     */
    public static float replaceAllPrices(List<String> list, int i) {
        float newPrice;
        String price = list.get(i + 2).replaceAll(".$", "");
        price = price.replaceAll(",", ".");
        newPrice = Float.parseFloat(price);
        return newPrice;
    }

    /**
     * Permet de vérifier si les donnèes dans le inputFile.JSON sont valides
     * ça va retourner true, au contraire ça va retourner false.
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Un boolean.
     */
    public static boolean valid(List<String> list) {
        return validClient(list) && validContract(list) && validTypeOfCare(list) && validDates(list) && validMonths(list)
                && validBetweenMonths(list);
    }

    /**
     * Permet de vérifier si les donnèes du client sont valides.
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Un boolean.
     */
    public static boolean validClient(List<String> list) {
        boolean isValid = true;
        String client = list.get(0);

        if (client.length() != 6) isValid = false;
        try {
            Integer.parseInt(client);
        } catch (NumberFormatException nfe) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * Permet de vérifier si les donnèes du contrat sont valides.
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Un boolean.
     */
    public static boolean validContract(List<String> list) {
        boolean isValid = true;
        String contract = list.get(1);

        if (!contract.equals("A") && !contract.equals("B") &&
                !contract.equals("C") && !contract.equals("D")) isValid = false;
        return isValid;
    }

    /**
     * Permet de vérifier si les tupes du soin sont valides.
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Un boolean.
     */
    public static boolean validTypeOfCare(List<String> list) {
        boolean isValid = true;
        for (int i = 3; i < list.size(); i += 3) {
            int care = Integer.parseInt(list.get(i));

            if (care < 300 || care > 399) {
                if (care != 0 && care != 100 && care != 200 && care != 400 && care != 500 && care != 600 && care != 700) isValid = false;
            }
        }
        return isValid;
    }

    /**
     * Permet de vérifier si les dates sont valides.
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Un boolean.
     */
    public static boolean validDates(List<String> list) {

        return list.get(2).matches("([0-9]{4})-([0-9]{2})");
    }

    /**
     * Permet de vérifier le format de la date.
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Un boolean.
     */
    public static boolean validMonths(List<String> list) {
        boolean isValid = true;
        for (int i = 4; i < list.size(); i += 3) {
            String dateCare = list.get(i);
            isValid = dateCare.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})");
        }
        return isValid;
    }

    /**
     * Vérifier le mois de la date si il est compatibles avec le mois du soin
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @return Un boolean.
     */
    public static boolean validBetweenMonths(List<String> list) {
        boolean isValid = true;
        String months1 = list.get(2).substring(5, 7);
        for (int i = 4; i < list.size(); i += 3) {
            String months2 = list.get(i).substring(5, 7);

            if (months1.equals(months2)) isValid = true;
            else {
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
     * @param filename le nom du fichier à modifier.
     * @throws IOException la classe de base pour les exceptions lancées lors de l'accès aux informations à l'aide de fichiers et de répertoires.
     */
    public static void JSONMessageError(String filename) throws IOException {
        JSONObject messageError = new JSONObject();
        messageError.put("message", "Données invalides");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String error = gson.toJson(messageError, LinkedHashMap.class);
            Files.write(Paths.get(filename), error.getBytes());

    }

    /**
     * Creer un nouveau fichier qui contient le résultat final
     * Si les donnèes entreès sont valides.
     *
     * @param filename le nom du fichier à modifier.
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @param newList la liste modifiée selon toutes les exigences.
     * @throws IOException la classe de base pour les exceptions lancées lors de l'accès aux informations à l'aide de fichiers et de répertoires.
     * @return un fichier JSONObject.
     */
    public static LinkedHashMap JSONOutputfile(String filename, List<String> list, List<String> newList) throws IOException {
        List<String> finalList = GetLastValue(list, newList);
        JSONArray refunds = addArrayToJSON(finalList);
        LinkedHashMap outputObj = addObjectToJSON(list, refunds);
        String refundsString = formatJSON(outputObj);
            Files.write(Paths.get(filename), refundsString.getBytes());

        return outputObj;
    }

    /**
     * Recuperer les elements de list et newList et les ajoutes dans la list finalList
     *
     * @param list La liste ou on a stocké les objets reçus du fichier inputfile.json
     * @param newList la liste modifiée selon toutes les exigences.
     * @return un List<String> finalList
     */
    public static List<String> GetLastValue(List<String> list, List<String> newList) {
        List<String> finalList = new ArrayList<>();
        finalList.add(0, list.get(0));
        finalList.add(1, list.get(2));

        for (int i = 3, j = 0; i < list.size() && j < newList.size(); i++) {

            if ((i - 2) % 3 == 0) {
                finalList.add(newList.get(j));
                j++;
            } else {
                finalList.add(list.get(i));
            }
        }
        return finalList;
    }

    /**
     * Ajouter les elements JSONArray dans le fichier JSON
     *
     * @param finalList la liste finale avec les valeurs modifiées.
     * @return un fichier JSONObject
     */
    public static JSONArray addArrayToJSON(List<String> finalList) {
        JSONArray refunds = new JSONArray();
        for (int i = 2; i < finalList.size(); i += 3) {
            LinkedHashMap refund = new LinkedHashMap();
            int care = Integer.parseInt(finalList.get(i));
            refund.put("soin", care);
            refund.put("date", finalList.get(i + 1));
            refund.put("montant", finalList.get(i + 2) + "$");
            refunds.add(refund);
        }
        return refunds;
    }

    /**
     * Ajouter les elements JSONObject dans le fichier JSON
     *
     * @param list la liste ou on a les valeurs exactes.
     * @param refunds toutes les valeurs du remboursement.
     * @return un fichier JSONObject.
     */
    public static LinkedHashMap addObjectToJSON(List<String> list, JSONArray refunds) {
        LinkedHashMap outputObj = new LinkedHashMap();
        outputObj.put("client", list.get(0));
        outputObj.put("mois", list.get(2));
        outputObj.put("remboursements", refunds);
        return outputObj;
    }

    /**
     * Formater le fichier JSON.
     *
     * @param outputObj le fichier jason formaté.
     * @return un fichier JSONObject bien formater.
     */
    public static String formatJSON(LinkedHashMap outputObj) {
        JSONObject object = new JSONObject();
        object.putAll(outputObj);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(outputObj, LinkedHashMap.class);
    }
}