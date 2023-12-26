package br.com.fipe.FipeTable.main;

import br.com.fipe.FipeTable.model.Data;
import br.com.fipe.FipeTable.model.Models;
import br.com.fipe.FipeTable.model.Vehicle;
import br.com.fipe.FipeTable.service.ApiConsumption;
import br.com.fipe.FipeTable.service.DataConversion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    Scanner scanner = new Scanner(System.in);

    private ApiConsumption consumption = new ApiConsumption();
    private DataConversion convert = new DataConversion();
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    public void displayMenu() {

        var menu = """
                
                *** OPTIONS ***
                Carro
                Moto
                Caminhão
                
                Type one of the options to consult:
                
                """;
        System.out.println(menu);
        var option = scanner.nextLine();
        String address;

        if (option.toLowerCase().contains("ca")) {
            address = URL_BASE + "carros/marcas";
        } else if (option.toLowerCase().contains("mot")) {
            address = URL_BASE + "motos/marcas";
        } else {
            address = URL_BASE + "caminhoes/marcas";
        }

        var json = consumption.dataObtain(address);
        System.out.println(json);
        var brands = convert.listObtain(json, Data.class);
        brands.stream()
                .sorted(Comparator.comparing(Data::code))
                .forEach(System.out::println);

        System.out.println("Inform your brand code to consult: ");
        var brandCode = scanner.nextLine();

        address = address + "/" + brandCode + "/modelos";
        json = consumption.dataObtain(address);
        var listModel = convert.dataObtain(json, Models.class);

        System.out.println("\nModels of the brand you chose: ");
        listModel.models().stream()
                .sorted(Comparator.comparing(Data::code))
                .forEach(System.out::println);

        System.out.println("Type an excerpt of the car's name: ");
        var vehicleName = scanner.nextLine();

        List<Data> filteredModels = listModel.models().stream()
                .filter(m -> m.name().toLowerCase().contains(vehicleName.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nFiltered Models");
        filteredModels.forEach(System.out::println);

        System.out.println("Type the model's code to search rating's values: ");
        var modelCode = scanner.nextLine();

        address = address + "/" + modelCode + "/anos";
        json = consumption.dataObtain(address);
        List<Data> years = convert.listObtain(json, Data.class);
        List<Vehicle> vehicles = new ArrayList<>();

        for (int i =0; i < years.size(); i++) {
            var yearAddress = address + "/" + years.get(i).code();
            json = consumption.dataObtain(yearAddress);
            Vehicle vehicle = convert.dataObtain(json, Vehicle.class);
            vehicles.add(vehicle);
        }

        System.out.println("All vehicles filtered with rating by year: ");
        vehicles.forEach(System.out::println);
    }
}
