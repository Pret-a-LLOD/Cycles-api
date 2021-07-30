# Cycles method

This project computes the cycle-based method to obtain translation pairs between two languages in order to get indirect translations. 
		
This method has participate in the [TIAD task](http://tiad2020.unizar.es/) (Translation inference across dictionaries) in 2020 in combination with the OTIC method, showing good results in comparison with other participants [1].

This technique was proposed by [2] in 2006. The idea was exploting the properties of the Apertium RDF Graph, by using cycles to identify potential targets that may be a translation of a given word.

Currently, the **REST Api** has the following endpoints:

1. `GET /translation/{source}-{target}/{entry}` , e.g. `/translation/en-fr/dog` Get an indirect translation from a given lexical entry by specifying the language codes of source and target languages, 
2. `GET /translation/{source}-{target}?format={format}`, e.g. `/translation/en-fr?format=tsv` Get a full bilingual dictionary generated from the execution of the algorithm with a given source and target languages and (optionally) store the result in a tsv file. The completion of this process might take more than 24h. The query parameter `format` is optional.  

4. `GET /translation/test/{source}-{target}?format={format}`, e.g. `/translation/test/en-fr` Used for testing purposes, generates only the first 4 entries of the bilingual dictionary (previous endpoint). The query parameter `format` is optional.  

For more details, please check the Swagger documentation at the path `/swagger-ui.html.` 

The data on which Cycle Rest API relies to infer new translations is the new RDF version of the Apertium family of dictionaries [3] . 

## Installation 

Requirements: To run this API locally, please ensure that you have Java and the build automation tool Maven installed. 

To install the project locally, follow these steps: 

1. In the root directory, run the command `mvn install` . 
2. Once the project has been successfully built, run in the same directory the command `java -jar target/cycles-0.0.1-SNAPSHOT.jar` to start the server.
3. Access the API at http://localhost:8080 . 

As an alternative, there is a Docker image at https://hub.docker.com/r/pretallod/link-cycles. 



## References
[1] Lanau-Coronas, M., & Gracia, J. (2020, May). Graph Exploration and Cross-lingual Word Embeddings for Translation Inference Across Dictionaries. In Proceedings of the 2020 Globalex Workshop on Linked Lexicography (pp. 106-110).

[2] Villegas, M., Melero, M., Bel, N., & Gracia, J. (2016, May). Leveraging RDF graphs for crossing multiple bilingual dictionaries. In Proceedings of the Tenth International Conference on Language Resources and Evaluation (LREC'16) (pp. 868-876).
