

import java.util.ArrayList;
import java.util.List;

//
public class Database {

    private static List<String> sponsored = new ArrayList<>();
    private static List<Apartment> apartments = new ArrayList<>();

    public static List<String> getSponsored() {
        return sponsored;
    }

    public static void addSponsored(String apartmentId) {
        sponsored.add(apartmentId);
    }

    public static void deleteSponsored(String apartmentId) {
        for (String s : sponsored) {
            if (s.equals(apartmentId)) {
                sponsored.remove(s);
                break;
            }
        }
    }

    //apartments
    public static void deleteApartment(String apartmentId) {
        for (Apartment apartment : apartments) {
            if (apartment.getId().equals(apartmentId)) {
                apartments.remove(apartment);
                break;
            }
        }
    }

    public static Apartment getApartment(String apartmentId) {
        for (Apartment apartment : apartments) {
            if (apartment.getId().equals(apartmentId))
                return apartment;
        }
        return null;
    }

    public static void addApartment(Apartment apartment) {
        apartments.add(apartment);
    }

    public static List<Apartment> getApartmentByCustomerId(String customerId) {
        List<Apartment> filteredApartments = new ArrayList<Apartment>();

        for (Apartment apartment : apartments) {
            if (apartment.getCustomerId().equals(customerId)) {
                System.out.println(apartment.toString());
                filteredApartments.add(apartment);
            }
        }
        return filteredApartments;
    }
}
