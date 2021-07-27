package sid.cycles.cycles.app.controller;

import sid.cycles.cycles.common.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import sid.cycles.cycles.common.apertiumV2.GetDataV2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@RestController
@Tag(name = "translation")
public class TranslationController {
	
	@Operation(description = "Given an enry, and source and target languages, returns a list of translations for the given entry in the source language (in the form of CycleDensity objects)")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "An array of CycleDensity instances") })
	@RequestMapping(path = "/translation/{source}-{target}/{entry}", method = RequestMethod.GET)
	public CycleDensity translationsFromEntry (
			@Parameter(example = "en") @PathVariable(value = "source") String source, 
			@Parameter(example = "fr") @PathVariable (value = "target") String target, 
			@Parameter(example = "dog") @PathVariable(value = "entry") String entry) {
		
		String langS = source;
		String langT = target;	
		String root = entry;
		ArrayList <CycleDensity> cycles;
		Context context = GetDataV2.getContext(root, langS, langT);
		cycles = CalculateCycles.getCycles(root, context, langS, langT);
		CycleDensity finalCycle = null;
		for (CycleDensity c : cycles) {
			if (c.getTarget().substring(c.getTarget().lastIndexOf("-") + 1).equals(langT)) {
				finalCycle = c;		
			}
		}
				
		return finalCycle;
		}

		
		@Operation(description = "Given source and target languages, returns a whole inferred bilingual dictionary. Results can be optionally saved as tsv. This might take a long time to run")
		@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "An array of CycleDensity instances") })
		@RequestMapping(path = "/translation/{source}-{target}", method = RequestMethod.GET)
		public ArrayList<CycleDensity> inferredBilingualDict (
				@Parameter(example = "en") @PathVariable(value = "source") String source, 
				@Parameter(example = "fr") @PathVariable (value = "target") String target, 
				@Parameter(example = "tsv") @RequestParam(value = "format", required =false) String format) {	
			String [] langS = {source};
			String [] langT = {target};
			String root;
			ArrayList<CycleDensity> dictionaryCycles = new ArrayList<CycleDensity>();
			GetDataV2.createLexiconFile(source);
			try {
				for (int i = 0; i < langS.length; i++) {
					String inputFile = "data/lexica/lexicon-" + langS[i] + ".txt";
					BufferedReader br = new BufferedReader(new FileReader(inputFile));
					String outputFile = "data/outputTranslations/cycles_ " + langS[i] + "-" + langT[i] + "_APv2.tsv";
					PrintWriter writer = new PrintWriter(new File(outputFile), "UTF-8");
				
					while ((root = br.readLine()) != null) {
						Context context = GetDataV2.getContext(root, source, target);
						ArrayList<CycleDensity> lineCycles;
						lineCycles = CalculateCycles.getCycles(root, context, source, target);
						CycleDensity finalCycle=null;
						if (!lineCycles.isEmpty()) {
							for (CycleDensity c : lineCycles) {
								if (c.getTarget().substring(c.getTarget().lastIndexOf("-") + 1).equals(target)) {
									finalCycle = c;
									if (format.equalsIgnoreCase("tsv")){
										String posLexinfo = context.getPairs().get(0).getPos();
										String posApertium = context.getPairs().get(0).getPosApertium();
										c.printToFile(writer, root, posLexinfo, posApertium); 
									}
								}
							}
							dictionaryCycles.add(finalCycle);
						};						
					}			
					
					br.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			return dictionaryCycles;
		}
		
		@Operation(description = "A test endpoint for inferredBilingualDict to infer only the first four entries in the bilingual dictionary")
		@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "An array of four CycleDensity instances") })
		@RequestMapping(path = "/translation/test/{source}-{target}", method = RequestMethod.GET)
		public ArrayList<CycleDensity> inferredBilingualDictTest (
				@Parameter(example = "en") @PathVariable(value = "source") String source, 
				@Parameter(example = "fr") @PathVariable (value = "target") String target, 
				@Parameter(example = "tsv") @RequestParam(value = "format", required =false) String format) {	
			String [] langS = {source};
			String [] langT = {target};
			String root;
			ArrayList<CycleDensity> dictionaryCycles = new ArrayList<CycleDensity>();
			GetDataV2.createLexiconFile(source);
			try {
				for (int i = 0; i < langS.length; i++) {
					String inputFile = "data/lexica/lexicon-" + langS[i] + ".txt";
					BufferedReader br = new BufferedReader(new FileReader(inputFile));
					String outputFile = "data/outputTranslations/cycles_ " + langS[i] + "-" + langT[i] + "_APv2.tsv";
					PrintWriter writer = new PrintWriter(new File(outputFile), "UTF-8");
					while ((root = br.readLine()) != null && dictionaryCycles.size()<4) {
						Context context = GetDataV2.getContext(root, source, target);
						ArrayList<CycleDensity> lineCycles;
						lineCycles = CalculateCycles.getCycles(root, context, source, target);
						CycleDensity finalCycle=null;
						if (!lineCycles.isEmpty()) {
							for (CycleDensity c : lineCycles) {
								if (c.getTarget().substring(c.getTarget().lastIndexOf("-") + 1).equals(target)) {
									finalCycle = c;
									if (format != null){
										if (format.equalsIgnoreCase("tsv")){
											String posLexinfo = context.getPairs().get(0).getPos();
											String posApertium = context.getPairs().get(0).getPosApertium();
											c.printToFile(writer, root, posLexinfo, posApertium); 
										}
									}
								}
							}
							dictionaryCycles.add(finalCycle);
						};						
					}			
					
					br.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			return dictionaryCycles;
		}
}
		







	