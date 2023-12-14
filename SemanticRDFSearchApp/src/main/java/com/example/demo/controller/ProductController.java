package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Product;

@Controller
@RequestMapping
public class ProductController {
    private static final String RDF_DATA_FILE = "./rdf/product_data.ttl"; // Replace with the path to your RDF data file
    private static final String RDF_DATA_NS = "http://example.org/amazon#";

    @GetMapping("/")
    public String index(Model model, @RequestParam(name = "query", required = false) String query) {
        if (query == null) {
            return "index"; 
        }

        // Create a Jena model and load the RDF data from the file
        Dataset dataset = DatasetFactory.create(RDF_DATA_FILE);
        org.apache.jena.rdf.model.Model rdfModel = dataset.getDefaultModel();

        // Construct a SPARQL query to search for products based on the user's input
        String queryString = "PREFIX ex: <" + RDF_DATA_NS + ">\n" +
                            "SELECT ?title ?price ?category WHERE {\n" +
                            "    ?product ex:hasTitle ?title .\n" +
                            "    ?product ex:hasPrice ?price .\n" +
                            "    ?product ex:belongsToCategory ?category .\n" +
                            "    FILTER (regex(?title, \"" + query + "\", \"i\"))\n" +
                            "}";

        Query qry = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(qry, rdfModel);

        ResultSet results = qexec.execSelect();

        List<Product> productList = new ArrayList<>();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Product product = new Product(
            		soln.getLiteral("title").getString(),
                    soln.getLiteral("price").getFloat(),
                    soln.getLiteral("category").getString()
                );
            productList.add(product);
        }

        model.addAttribute("products", productList);
        qexec.close();

        return "index";
    }
}
