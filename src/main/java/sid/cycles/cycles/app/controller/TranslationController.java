package sid.cycles.cycles.app.controller;

import sid.cycles.cycles.common.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sid.cycles.cycles.common.apertiumV2.GetDataV2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@RestController
public class TranslationController {
	
	@RequestMapping(path = "/translation/{source}-{target}/{entry}", method = RequestMethod.GET)
	public CycleDensity translationsFromEntry (
			@PathVariable(value = "source") String source, @PathVariable (value = "target") String target, 
			@PathVariable(value = "entry") String entry) {
		
		String langS = source;
		String langT = target;	
		String root = entry;
		ArrayList <CycleDensity> cycles;
		Context context = GetDataV2.getContext(root, langS, langT);
		cycles = CalculateCycles.getCycles(root, context, langS, langT);
		CycleDensity finalCycle = null;
		for (CycleDensity c : cycles) {
			if (c.getTarget().substring(c.getTarget().lastIndexOf("-") + 1).equals(langT)) {
				//c.print();
				finalCycle = c;
				
			}
		}
				
		return finalCycle;
		}
//	}
	/*
	 * @RequestMapping(path = "/translation/{source}-{target}/tsv", method =
	 * RequestMethod.GET) public void translationsFromEntryTSV (
	 * 
	 * @PathVariable(value = "source") String source, @PathVariable (value =
	 * "target") String target) { String [] langS = {source}; String [] langT =
	 * {target}; String root; GetDataV2.createLexiconFile(source); try { for (int i
	 * = 0; i < langS.length; i++) { String inputFile = "data/lexica/lexicon-" +
	 * langS[i] + ".txt"; String outputFile = "data/outputTranslations/cycles_ " +
	 * langS[i] + "-" + langT[i] + "_APv2.tsv"; BufferedReader br = new
	 * BufferedReader(new FileReader(inputFile)); PrintWriter writer = new
	 * PrintWriter(new File(outputFile), "UTF-8"); while ((root = br.readLine()) !=
	 * null) { CalculateCycles.getCycles(root, GetDataV2.getContext(root, langS[i],
	 * langT[i]), langS[i], langT[i], writer); } br.close(); writer.close(); }
	 * }catch(IOException e){ e.printStackTrace(); } }
	 */
		
		@RequestMapping(path = "/translation/{source}-{target}", method = RequestMethod.GET)
		public ArrayList<CycleDensity> inferredBilingualDict (
				@PathVariable(value = "source") String source, @PathVariable (value = "target") String target, @RequestParam(value = "format", required =false) String format) {	
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
}
		







	