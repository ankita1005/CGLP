package courtreferences.controller;

public class DocumentParserFactory {
	public static DocumentParser getDocumentParser(String fileType, String countryname, String courtname, String processedUser){
		if("pdf".equalsIgnoreCase(fileType)){
			if(countryname.equalsIgnoreCase("Malaysia|Malaysian")){	
				System.out.println("In Malay");
				return new MalaysiaPdfParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Nigeria|Nigerian")){
				
				return new NigeriaPdfParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Belgium|Belgian")){
				
				return new BelgiumPdfParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("South Africa|South African")){
				System.out.println("hey i am sa");
				return new SouthAfricaPdfParser(countryname, courtname, processedUser);
			}
			else{
				return new PdfParser(countryname, courtname, processedUser);
			}
		}else{
			if(countryname.equalsIgnoreCase("Canada|Canadian")){
				return new CanadaHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Australia|Australian")){
				return new AustralianHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Philippines|Philippine")){
				return new PhilipinesHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("United States|American")){
				return new USAHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("India|Indian")){
				return new IndiaHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Ireland|Irish")){
				
				return new IrelandHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("New Zealand|New Zealand")){
				
				return new NewZealandHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Botswana|Motswana| Botswanan| Herzegovinian")){
				
				return new BotswanaHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("United Kingdom|British|English") && courtname.equalsIgnoreCase("House Of Lords")){
				
				return new UKHouseOfLordsHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("United Kingdom|British|English") && courtname.equalsIgnoreCase("Supreme Court")){
				
				return new UKSupremeCourtHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Chile|Chilean")){
				
				return new ChileHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Colombia|Colombian")){
				
				return new ColombiaHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Peru|Peruvian")){
				
				return new PeruHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Spain|Spanish")){
				
				return new SpainHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Belarus|Belarusian")){
				
				return new BelarusHTMLParser(countryname, courtname, processedUser);
			}
			
		
			else if(countryname.equalsIgnoreCase("Zimbabwe|Zimbabwean")){
				
				return new ZimbabweHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Latvia|Latvian")){
				
				return new LatviaHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Lesotho|Basotho")){
				
				return new LesothoHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Malawi|Malawian")){
				
				return new MalawiHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Papua New Guinea|Papua New Guinean")){
				
				return new PapuaHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Uganda|Ugandan")){
				
				return new UgandaHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Austria|Austrian")){
				
				return new AustriaHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Germany|German")){
	
				return new GermanyHTMLParser(countryname, courtname, processedUser);
			}
			else if(countryname.equalsIgnoreCase("Switzerland|Swiss")){
				
				return new SwitzerlandHTMLParser(countryname, courtname, processedUser);
			}
			
			else if(countryname.equalsIgnoreCase("France|French")){
				
				return new FranceHTMLParser(countryname, courtname, processedUser);
			}
			
			
			else 
				return null;
		}
	}
}
