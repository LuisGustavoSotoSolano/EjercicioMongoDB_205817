/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongodbtest;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.bson.Document;

/**
 *
 * @author Luis
 */
public class Mongodbtest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        MongoClient mongo = new MongoClient("localhost");
        MongoDatabase database = mongo.getDatabase("restaurante");
//        System.out.println(database.listCollectionNames());

        MongoCollection<Document> collection = database.getCollection("restaurantes");
        Document restaurante1 = new Document()
                .append("id", 1)
                .append("nombre", "sushiniko")
                .append("rating", 4)
                .append("catagoria", Arrays.asList("rollo", "bebida"))
                .append("direccion", "Calle 300 4312");

        Document restaurante3 = new Document()
                .append("id", 3)
                .append("nombre", "Los Alamos Café")
                .append("rating", 5)
                .append("catagoria", Arrays.asList("platillo", "sopa", "cafe"))
                .append("direccion", "Calle Sinaloa 167");
        
        Document restaurante2 = new Document()
                .append("id", 2)
                .append("nombre", "domminos's pizza")
                .append("rating", 2)
                .append("catagoria", Arrays.asList("pizza", "postre"))
                .append("direccion", "Blvd. Rodolfo Elías Calles 1651");


        collection.insertMany(Arrays.asList(restaurante1, restaurante2, restaurante3));

//        for (Document doc : collection.find()) {
//            System.out.println(doc.toJson());
//        }
        FindIterable<Document> documents = collection.find(
                Filters.gt("rating", 4)
        );
        System.out.println("Rating > 4");
        for (Document doc : documents) {
            System.out.println(doc.toJson());
        }
        documents = collection.find(
                Filters.eq("catagoria", "pizza")
        );
        System.out.println("Categoría pizza");
        for (Document doc : documents) {
            System.out.println(doc.toJson());
        }
        documents = collection.find(
                Filters.regex("nombre", Pattern.compile("sushi", Pattern.CASE_INSENSITIVE))
        );
        System.out.println("sushi nombre");
        for (Document doc : documents) {
            System.out.println(doc.toJson());
        }

        System.out.println("Agregar una categoría extra al restaurant sushilito");
        UpdateResult updateResult = collection.updateOne(Filters.eq("nombre", "sushiniko"), Updates.addToSet("categoria", "adereso"));
        System.out.println("Se actualizaron: "+updateResult.getModifiedCount());
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
        
        System.out.println("Eliminar un restaurante por su identificador");

        DeleteResult deleteResult = collection.deleteOne(Filters.eq("id", 3));
        System.out.println("Se eliminaron: "+deleteResult.getDeletedCount());
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
        System.out.println("Eliminar los restaurantes con 3 estrellas o menos");
        deleteResult = collection.deleteMany(Filters.lte("rating", 2));
        System.out.println("Se eliminaron: "+deleteResult.getDeletedCount());
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
    }

}
