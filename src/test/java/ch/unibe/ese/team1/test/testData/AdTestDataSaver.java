package ch.unibe.ese.team1.test.testData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.Bet;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.BetDao;
import ch.unibe.ese.team1.model.dao.UserDao;

/** This inserts several ad elements into the database. */
@Service
public class AdTestDataSaver {

	@Autowired
	private AdDao adDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private BetDao betDao;
	
	@Transactional
	public void saveTestData() throws Exception {
		User bernerBaer = userDao.findByUsername("user@bern.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		User oprah = userDao.findByUsername("oprah@winfrey.com");
		User jane = userDao.findByUsername("jane@doe.com");
		User hans = userDao.findByUsername("hans@unibe.ch");
		User mathilda = userDao.findByUsername("mathilda@unibe.ch");
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		
		Date creationDate1 = formatter.parse("03.10.2014");
		Date creationDate2 = formatter.parse("11.10.2014");
		Date creationDate3 = formatter.parse("25.10.2014");
		Date creationDate4 = formatter.parse("02.11.2014");
		Date creationDate5 = formatter.parse("25.11.2013");
		Date creationDate6 = formatter.parse("01.12.2014");
		Date creationDate7 = formatter.parse("16.11.2014");
		Date creationDate8 = formatter.parse("27.11.2014");
		
		Date moveInDate1 = formatter.parse("15.12.2014");
		Date moveInDate2 = formatter.parse("21.12.2014");
		Date moveInDate3 = formatter.parse("01.01.2015");
		Date moveInDate4 = formatter.parse("15.01.2015");
		Date moveInDate5 = formatter.parse("01.02.2015");
		Date moveInDate6 = formatter.parse("01.03.2015");
		Date moveInDate7 = formatter.parse("15.03.2015");
		Date moveInDate8 = formatter.parse("16.02.2015");
		
		String houseDescription1 = "The house is a part of 3.5 houses apartment completely renovated"
				+ "in 2010 at Kramgasse, Bern. The apartment is about 50 m2 on 1st floor."
				+ "Apt is equipped modern kitchen, hall and balcony. Near to shops and public"
				+ "transportation. Monthly rent is 500 CHF including charges. Internet + TV + landline"
				+ "charges are separate. If you are interested, feel free to drop me a message"
				+ "to have an appointment for a visit or can write me for any further information";

		Ad adBern = new Ad();
		adBern.setZipcode(3011);
		adBern.setMoveInDate(moveInDate1);
		adBern.setCreationDate(creationDate1);
		adBern.setPrice(400);
		adBern.setSquareFootage(50);
		adBern.setFlat(false);
		adBern.setSmokers(false);
		adBern.setAnimals(true);
		adBern.setHouseDescription(houseDescription1);
		adBern.setUser(bernerBaer);
		adBern.setTitle("Dude wanted in Bern");
		adBern.setStreet("Kramgasse 22");
		adBern.setCity("Bern");
		adBern.setGarden(true);
		adBern.setBalcony(true);
		adBern.setCellar(true);
		adBern.setFurnished(true);
		adBern.setCable(true);
		adBern.setGarage(true);
		adBern.setOnHomepage(true);
		List<AdPicture> pictures = new ArrayList<>();
		pictures.add(createPicture(adBern, "/img/test/ad1_1.jpg"));
		pictures.add(createPicture(adBern, "/img/test/ad1_2.jpg"));
		pictures.add(createPicture(adBern, "/img/test/ad1_3.jpg"));
		adBern.setPictures(pictures);
		adDao.save(adBern);

		String flatDescription2 = "It is small flat close to the"
				+ "university and the bahnhof. The lovely neighbourhood"
				+ "Langgasse makes it an easy place to feel comfortable."
				+ "The flat is close to a Migross, Denner and the Coop."
				+ "The flat is 60m2. It has it own Badhouse and kitchen."
				+ "Nothing is shared. The flat is fully furnished. The"
				+ "flat is also provided with a balcony. So if you want to"
				+ "have a privat space this could totally be good place for you."
				+ "Be aware it is only till the end of March. The price is from"
				+ "550- 700 CHF, But there is always house to talk about it.";
		
		Ad adBern2 = new Ad();
		adBern2.setZipcode(3012);
		adBern2.setMoveInDate(moveInDate2);
		adBern2.setCreationDate(creationDate2);
		adBern2.setPrice(700);
		adBern2.setSquareFootage(60);
		adBern2.setFlat(true);
		adBern2.setSmokers(false);
		adBern2.setAnimals(true);
		adBern2.setHouseDescription(flatDescription2);
		adBern2.setUser(ese);
		adBern2.setTitle("Cheap flat in Bern!");
		adBern2.setStreet("Längassstr. 40");
		adBern2.setCity("Bern");
		adBern2.setGarden(false);
		adBern2.setBalcony(false);
		adBern2.setCellar(false);
		adBern2.setFurnished(false);
		adBern2.setCable(false);
		adBern2.setGarage(false);
		adBern2.setOnHomepage(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adBern2, "/img/test/ad2_1.jpg"));
		pictures.add(createPicture(adBern2, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adBern2, "/img/test/ad2_3.jpg"));
		adBern2.setPictures(pictures);
		adDao.save(adBern2);

		String flatDescription3 = " In the center of Gundeli (5 Min. away from the"
				+ "station, close to Tellplatz) there is a lovely house, covered with"
				+ "savage wine stocks, without any neighbours but with a garden. The"
				+ "house has two storey, on the first floor your new house is waiting"
				+ "for you. The house is totally equipped with everything a household "
				+ ": washing machine, kitchen, bathouse, W-Lan...if you don´t have any"
				+ "furniture, don´t worry, I am sure, we will find something around"
				+ "the house. The price for the house and all included is 480 CHF /month. "
				+ " (29, Graphic designer) and Linda (31, curator) are looking for a"
				+ "new female flatmate from December on.";
		
		Ad adBasel = new Ad();
		adBasel.setZipcode(4051);
		adBasel.setMoveInDate(moveInDate3);
		adBasel.setCreationDate(creationDate3);
		adBasel.setPrice(480);
		adBasel.setSquareFootage(10);
		adBasel.setFlat(true);
		adBasel.setSmokers(true);
		adBasel.setAnimals(false);
		adBasel.setHouseDescription(flatDescription3);
		adBasel.setUser(bernerBaer);
		adBasel.setTitle("Nice, bright flat in the center of Basel");
		adBasel.setStreet("Bruderholzstrasse 32");
		adBasel.setCity("Basel");
		adBasel.setGarden(false);
		adBasel.setBalcony(false);
		adBasel.setCellar(false);
		adBasel.setFurnished(false);
		adBasel.setCable(false);
		adBasel.setGarage(false);
		adBasel.setOnHomepage(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adBasel, "/img/test/ad3_1.jpg"));
		pictures.add(createPicture(adBasel, "/img/test/ad3_2.jpg"));
		pictures.add(createPicture(adBasel, "/img/test/ad3_3.jpg"));
		adBasel.setPictures(pictures);
		adBasel.setDistanceToNearestPublicTransport(300);
		adBasel.setRunningCosts(100);
		adDao.save(adBasel);
		
		String flatDescription4 = "Flatshare of 3 persons. Flat with 5 houses"
				+ "on the second floor. The bedhouse is about 60 square meters"
				+ "with access to a nice balcony. In addition to the house, the"
				+ "flat has: a living house, a kitchen, a bathhouse, a seperate WC,"
				+ "a storage in the basement, a balcony, a laundry house in the basement."
				+ "The bedhouse is big and bright and has a nice parquet floor."
				+ "Possibility to keep some furnitures like the bed.";
		
		Ad adOlten = new Ad();
		adOlten.setZipcode(4600);
		adOlten.setMoveInDate(moveInDate4);
		adOlten.setCreationDate(creationDate4);
		adOlten.setPrice(430);
		adOlten.setSquareFootage(60);
		adOlten.setNumberOfRooms(1);
		adOlten.setFlat(false);
		adOlten.setSmokers(true);
		adOlten.setAnimals(false);
		adOlten.setHouseDescription(flatDescription4);
		adOlten.setUser(ese);
		adOlten.setTitle("House wanted in Olten City");
		adOlten.setStreet("Zehnderweg 5");
		adOlten.setCity("Olten");
		adOlten.setGarden(false);
		adOlten.setBalcony(true);
		adOlten.setCellar(true);
		adOlten.setFurnished(true);
		adOlten.setCable(true);
		adOlten.setGarage(false);
		adOlten.setOnHomepage(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adOlten, "/img/test/ad4_1.png"));
		pictures.add(createPicture(adOlten, "/img/test/ad4_2.png"));
		pictures.add(createPicture(adOlten, "/img/test/ad4_3.png"));
		adOlten.setPictures(pictures);
		adOlten.setDistanceToNearestSchool(30);
		adDao.save(adOlten);

		String flatDescription5 = "Flat meublé au 3ème étage, comprenant"
				+ "une kitchenette entièrement équipée (frigo, plaques,"
				+ "four et hotte), une pièce à vivre donnant sur un balcon,"
				+ "une salle de bains avec wc. Cave, buanderie et site satellite"
				+ "à disposition.";
		
		Ad adNeuchâtel = new Ad();
		adNeuchâtel.setZipcode(2000);
		adNeuchâtel.setMoveInDate(moveInDate5);
		adNeuchâtel.setCreationDate(creationDate5);
		adNeuchâtel.setPrice(410);
		adNeuchâtel.setSquareFootage(40);
		adNeuchâtel.setFlat(true);
		adNeuchâtel.setSmokers(true);
		adNeuchâtel.setAnimals(false);
		adNeuchâtel.setHouseDescription(flatDescription5);
		adNeuchâtel.setUser(bernerBaer);
		adNeuchâtel.setTitle("Flat extrèmement bon marché à Neuchâtel");
		adNeuchâtel.setStreet("Rue de l'Hôpital 11");
		adNeuchâtel.setCity("Neuchâtel");
		adNeuchâtel.setGarden(true);
		adNeuchâtel.setBalcony(false);
		adNeuchâtel.setCellar(true);
		adNeuchâtel.setFurnished(true);
		adNeuchâtel.setCable(false);
		adNeuchâtel.setGarage(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_1.jpg"));
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_2.jpg"));
		pictures.add(createPicture(adNeuchâtel, "/img/test/ad5_3.jpg"));
		adNeuchâtel.setPictures(pictures);
		adDao.save(adNeuchâtel);

		String flatDescription6 = "A place just for yourself in a very nice part of Biel."
				+ "A flat for 1-2 persons with a big balcony, bathhouse, kitchen and furniture already there."
				+ "It's quiet and nice, very close to the old city of Biel.";
		
		Ad adBiel = new Ad();
		adBiel.setZipcode(2503);
		adBiel.setMoveInDate(moveInDate6);
		adBiel.setCreationDate(creationDate6);
		adBiel.setPrice(480);
		adBiel.setSquareFootage(10);
		adBiel.setFlat(true);
		adBiel.setSmokers(true);
		adBiel.setAnimals(false);
		adBiel.setHouseDescription(flatDescription6);
		adBiel.setUser(ese);
		adBiel.setTitle("Direct by the lake. Nice flat");
		adBiel.setStreet("Oberer Quai 12");
		adBiel.setCity("Biel/Bienne");
		adBiel.setGarden(false);
		adBiel.setBalcony(false);
		adBiel.setCellar(false);
		adBiel.setFurnished(false);
		adBiel.setCable(false);
		adBiel.setGarage(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adBiel, "/img/test/ad6_1.png"));
		pictures.add(createPicture(adBiel, "/img/test/ad6_2.png"));
		pictures.add(createPicture(adBiel, "/img/test/ad6_3.png"));
		adBiel.setPictures(pictures);
		adDao.save(adBiel);
		
		
		String houseDescription7 = "The house is a part of 3.5 houses apartment completely renovated"
				+ "in 2010 at Kramgasse, Bern. The apartment is about 50 m2 on 1st floor."
				+ "Apt is equipped modern kitchen, hall and balcony. Near to shops and public"
				+ "transportation. Monthly rent is 500 CHF including charges. Internet + TV + landline"
				+ "charges are separate. If you are interested, feel free to drop me a message"
				+ "to have an appointment for a visit or can write me for any further information";

		Ad adZurich = new Ad();
		adZurich.setZipcode(8000);
		adZurich.setMoveInDate(moveInDate7);
		adZurich.setCreationDate(creationDate7);
		adZurich.setPrice(480);
		adZurich.setSquareFootage(32);
		adZurich.setFlat(false);
		adZurich.setSmokers(false);
		adZurich.setAnimals(false);
		adZurich.setHouseDescription(houseDescription7);
		adZurich.setUser(oprah);
		adZurich.setTitle("Dude wanted in Zürich");
		adZurich.setStreet("Hauptstrasse 61");
		adZurich.setCity("Zürich");
		adZurich.setGarden(false);
		adZurich.setBalcony(true);
		adZurich.setCellar(false);
		adZurich.setFurnished(true);
		adZurich.setCable(true);
		adZurich.setGarage(true);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adZurich, "/img/test/ad1_3.jpg"));
		pictures.add(createPicture(adZurich, "/img/test/ad1_2.jpg"));
		pictures.add(createPicture(adZurich, "/img/test/ad1_1.jpg"));
		adZurich.setPictures(pictures);
		adDao.save(adZurich);
	
		
		String flatDescription8 = "It is small flat close to the"
				+ "university and the bahnhof. The lovely neighbourhood"
				+ "Langgasse makes it an easy place to feel comfortable."
				+ "The flat is close to a Migross, Denner and the Coop."
				+ "The flat is 60m2. It has it own Badhouse and kitchen."
				+ "Nothing is shared. The flat is fully furnished. The"
				+ "flat is also provided with a balcony. So if you want to"
				+ "have a privat space this could totally be good place for you."
				+ "Be aware it is only till the end of March. The price is from"
				+ "550- 700 CHF, But there is always house to talk about it.";
		
		Ad adLuzern = new Ad();
		adLuzern.setZipcode(6000);
		adLuzern.setMoveInDate(moveInDate8);
		adLuzern.setCreationDate(creationDate2);
		adLuzern.setPrice(700);
		adLuzern.setSquareFootage(60);
		adLuzern.setFlat(true);
		adLuzern.setSmokers(false);
		adLuzern.setAnimals(false);
		adLuzern.setHouseDescription(flatDescription8);
		adLuzern.setUser(oprah);
		adLuzern.setTitle("Beatiful flat in lucerne");
		adLuzern.setStreet("Schwanenplatz 61");
		adLuzern.setCity("Luzern");
		adLuzern.setGarden(false);
		adLuzern.setBalcony(false);
		adLuzern.setCellar(false);
		adLuzern.setFurnished(false);
		adLuzern.setCable(false);
		adLuzern.setGarage(false);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLuzern, "/img/test/ad2_3.jpg"));
		pictures.add(createPicture(adLuzern, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adLuzern, "/img/test/ad2_1.jpg"));
		adLuzern.setPictures(pictures);
		adDao.save(adLuzern);

		String flatDescription9 = "In the center of Gundeli (5 Min. away from the"
				+ "station, close to Tellplatz) there is a lovely house, covered with"
				+ "savage wine stocks, without any neighbours but with a garden. The"
				+ "house has two storey, on the first floor your new house is waiting"
				+ "for you. The house is totally equipped with everything a household "
				+ ": washing machine, kitchen, bathouse, W-Lan...if you don´t have any"
				+ "furniture, don´t worry, I am sure, we will find something around"
				+ "the house. The price for the house and all included is 480 CHF /month. "
				+ " (29, Graphic designer) and Linda (31, curator) are looking for a"
				+ "new female flatmate from December on.";
		
		Ad adAarau = new Ad();
		adAarau.setZipcode(5000);
		adAarau.setMoveInDate(moveInDate3);
		adAarau.setCreationDate(creationDate8);
		adAarau.setPrice(800);
		adAarau.setSquareFootage(26);
		adAarau.setFlat(true);
		adAarau.setSmokers(true);
		adAarau.setAnimals(false);
		adAarau.setHouseDescription(flatDescription9);
		adAarau.setUser(oprah);
		adAarau.setTitle("Beautiful flat in Aarau");
		adAarau.setStreet("Bruderholzstrasse 32");
		adAarau.setCity("Aarau");
		adAarau.setGarden(false);
		adAarau.setBalcony(true);
		adAarau.setCellar(false);
		adAarau.setFurnished(true);
		adAarau.setCable(false);
		adAarau.setGarage(false);
		adAarau.setRunningCosts(300);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adAarau, "/img/test/ad3_3.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad3_2.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad3_1.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad2_2.jpg"));
		pictures.add(createPicture(adAarau, "/img/test/ad2_3.jpg"));
		adAarau.setDistanceToNearestPublicTransport(4000);
		adAarau.setDistanceToNearestSchool(20);
		
		adAarau.setPictures(pictures);
		adDao.save(adAarau);
		
		String flatDescription10 = "Flatshare of 3 persons. Flat with 5 houses"
				+ "on the second floor. The bedhouse is about 60 square meters"
				+ "with access to a nice balcony. In addition to the house, the"
				+ "flat has: a living house, a kitchen, a bathhouse, a seperate WC,"
				+ "a storage in the basement, a balcony, a laundry house in the basement."
				+ "The bedhouse is big and bright and has a nice parquet floor."
				+ "Possibility to keep some furnitures like the bed.";
		
		Ad adDavos = new Ad();
		adDavos.setZipcode(7260);
		adDavos.setMoveInDate(moveInDate2);
		adDavos.setCreationDate(creationDate4);
		adDavos.setPrice(30000);
		adDavos.setSquareFootage(74);
		adDavos.setNumberOfRooms(4);
		adDavos.setFlat(false);
		adDavos.setSmokers(true);
		adDavos.setAnimals(false);
		adDavos.setHouseDescription(flatDescription10);
		adDavos.setUser(oprah);
		adDavos.setTitle("Free house in Davos City");
		adDavos.setStreet("Kathrinerweg 5");
		adDavos.setCity("Davos");
		adDavos.setGarden(false);
		adDavos.setBalcony(true);
		adDavos.setCellar(true);
		adDavos.setFurnished(true);
		adDavos.setCable(true);
		adDavos.setGarage(false);
		adDavos.setRunningCosts(500);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adDavos, "/img/test/ad4_3.png"));
		pictures.add(createPicture(adDavos, "/img/test/ad4_2.png"));
		pictures.add(createPicture(adDavos, "/img/test/ad4_1.png"));
		adDavos.setDistanceToNearestPublicTransport(100);
		adDavos.setDistanceToNearestSchool(50);
		adDavos.setDistanceToNearestSuperMarket(100);
		adDavos.setPictures(pictures);
		adDao.save(adDavos);

		String flatDescription11 = "Flat meublé au 3ème étage, comprenant"
				+ "une kitchenette entièrement équipée (frigo, plaques,"
				+ "four et hotte), une pièce à vivre donnant sur un balcon,"
				+ "une salle de bains avec wc. Cave, buanderie et site satellite"
				+ "à disposition.";
		
		Ad adLausanne = new Ad();
		adLausanne.setZipcode(1000);
		adLausanne.setMoveInDate(moveInDate5);
		adLausanne.setCreationDate(creationDate5);
		adLausanne.setPrice(360);
		adLausanne.setNumberOfRooms(1);
		adLausanne.setSquareFootage(8);
		adLausanne.setFlat(false);
		adLausanne.setSmokers(true);
		adLausanne.setAnimals(false);
		adLausanne.setHouseDescription(flatDescription11);
		adLausanne.setUser(oprah);
		adLausanne.setTitle("Flat extrèmement bon marché à Lausanne");
		adLausanne.setStreet("Rue de l'Eglise 26");
		adLausanne.setCity("Lausanne");
		adLausanne.setGarden(true);
		adLausanne.setBalcony(false);
		adLausanne.setCellar(true);
		adLausanne.setFurnished(true);
		adLausanne.setCable(false);
		adLausanne.setGarage(false);
		adLausanne.setRunningCosts(200);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLausanne, "/img/test/ad5_3.jpg"));
		pictures.add(createPicture(adLausanne, "/img/test/ad5_2.jpg"));
		pictures.add(createPicture(adLausanne, "/img/test/ad5_1.jpg"));
		adLausanne.setPictures(pictures);
		adLausanne.setDistanceToNearestPublicTransport(2000);
		adLausanne.setDistanceToNearestSchool(1000);
		adLausanne.setDistanceToNearestSuperMarket(3000);
		adDao.save(adLausanne);

		String flatDescription12 = "A place just for yourself in a very nice part of Biel."
				+ "A flat for 1-2 persons with a big balcony, bathhouse, kitchen and furniture already there."
				+ "It's quiet and nice, very close to the old city of Biel.";
		
		Ad adLocarno = new Ad();
		adLocarno.setZipcode(6600);
		adLocarno.setMoveInDate(moveInDate6);
		adLocarno.setCreationDate(creationDate6);
		adLocarno.setPrice(960);
		adLocarno.setSquareFootage(42);
		adLocarno.setNumberOfRooms(4);
		adLocarno.setFlat(false);
		adLocarno.setSmokers(true);
		adLocarno.setAnimals(false);
		adLocarno.setHouseDescription(flatDescription12);
		adLocarno.setUser(jane);
		adLocarno.setTitle("Malibu-style Beachhouse");
		adLocarno.setStreet("Kirchweg 12");
		adLocarno.setCity("Locarno");
		adLocarno.setGarden(false);
		adLocarno.setBalcony(false);
		adLocarno.setCellar(false);
		adLocarno.setFurnished(false);
		adLocarno.setCable(false);
		adLocarno.setGarage(false);
		adLausanne.setRunningCosts(300);
		pictures = new ArrayList<>();
		pictures.add(createPicture(adLocarno, "/img/test/ad6_3.png"));
		pictures.add(createPicture(adLocarno, "/img/test/ad6_2.png"));
		pictures.add(createPicture(adLocarno, "/img/test/ad6_1.png"));
		adLocarno.setPictures(pictures);
		adLocarno.setDistanceToNearestPublicTransport(8000);
		adLocarno.setDistanceToNearestSchool(9000);
		adLocarno.setDistanceToNearestSuperMarket(10000);
		adDao.save(adLocarno);

		Ad adTestRent = new Ad();
		adTestRent.setZipcode(5000);
		adTestRent.setMoveInDate(moveInDate8);
		adTestRent.setCreationDate(creationDate2);
		adTestRent.setNumberOfRooms(4);
		adTestRent.setPrice(700);
		adTestRent.setSquareFootage(60);
		adTestRent.setFlat(true);
		adTestRent.setSmokers(false);
		adTestRent.setAnimals(false);
		adTestRent.setHouseDescription(flatDescription8);
		adTestRent.setUser(oprah);
		adTestRent.setTitle("Sweet flat for rent");
		adTestRent.setStreet("Schwanenplace 61A");
		adTestRent.setCity("Aarau");
		adTestRent.setGarden(false);
		adTestRent.setBalcony(false);
		adTestRent.setCellar(false);
		adTestRent.setFurnished(false);
		adTestRent.setCable(false);
		adTestRent.setGarage(false);
		adTestRent.setForSale(false);
		adLausanne.setRunningCosts(100);
		adTestRent.setDistanceToNearestPublicTransport(500);
		adTestRent.setDistanceToNearestSchool(400);
		adTestRent.setDistanceToNearestSuperMarket(500);
		adDao.save(adTestRent);
		
	
		Ad adTestSale = new Ad();
		adTestSale.setZipcode(5000);
		adTestSale.setMoveInDate(moveInDate8);
		adTestSale.setCreationDate(creationDate2);
		adTestSale.setPrice(1000);
		adTestSale.setSquareFootage(60);
		adTestSale.setNumberOfRooms(3);
		adTestSale.setFlat(true);
		adTestSale.setSmokers(false);
		adTestSale.setAnimals(false);
		adTestSale.setHouseDescription(flatDescription8);
		adTestSale.setUser(oprah);
		adTestSale.setTitle("Sweet Flat for Sale");
		adTestSale.setStreet("Schwanenplace 61B");
		adTestSale.setCity("Aarau");
		adTestSale.setGarden(false);
		adTestSale.setBalcony(false);
		adTestSale.setCellar(false);
		adTestSale.setFurnished(false);
		adTestSale.setCable(false);
		adTestSale.setGarage(false);
		adTestSale.setForSale(true);
		adTestSale.setRunningCosts(300);
		adTestSale.setDistanceToNearestPublicTransport(1);
		adTestSale.setDistanceToNearestSchool(1);
		adTestSale.setDistanceToNearestSuperMarket(2);
		adDao.save(adTestSale);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		Ad adWithAuction = new Ad();
		adWithAuction.setZipcode(5000);
		adWithAuction.setMoveInDate(moveInDate8);
		adWithAuction.setCreationDate(creationDate2);
		adWithAuction.setPrice(1000000);
		adWithAuction.setSquareFootage(60);
		adWithAuction.setNumberOfRooms(3);
		adWithAuction.setFlat(false);
		adWithAuction.setSmokers(false);
		adWithAuction.setAnimals(false);
		adWithAuction.setHouseDescription(flatDescription8);
		adWithAuction.setUser(oprah);
		adWithAuction.setTitle("Sweet House for Sale");
		adWithAuction.setStreet("Schwanenplace 61B");
		adWithAuction.setCity("Aarau");
		adWithAuction.setGarden(true);
		adWithAuction.setBalcony(true);
		adWithAuction.setCellar(true);
		adWithAuction.setFurnished(false);
		adWithAuction.setCable(false);
		adWithAuction.setGarage(true);
		adWithAuction.setForSale(true);
		adWithAuction.setRunningCosts(300);
		adWithAuction.setDistanceToNearestPublicTransport(1);
		adWithAuction.setDistanceToNearestSchool(1);
		adWithAuction.setDistanceToNearestSuperMarket(2);
		adWithAuction.setAuctionEndingDate(cal.getTime());
		adWithAuction.setAuctionStartingPrice(250000);
		adWithAuction.setOnHomepage(true);
		adDao.save(adWithAuction);

		/** place some bets **/
		Calendar calBets = Calendar.getInstance();
		calBets.add(Calendar.DATE, -7);
		Bet bet1 = new Bet();
		bet1.setAd(adWithAuction);
		bet1.setPrice(300000);
		bet1.setUser(ese);
		bet1.setCreationDate(calBets.getTime());
		
		calBets.add(Calendar.HOUR, 2);
		Bet bet2 = new Bet();
		bet2.setAd(adWithAuction);
		bet2.setPrice(350000);
		bet2.setUser(jane);
		bet2.setCreationDate(calBets.getTime());
		
		calBets.add(Calendar.HOUR, 2);
		Bet bet3 = new Bet();
		bet3.setAd(adWithAuction);
		bet3.setPrice(400000);
		bet3.setUser(ese);
		bet3.setCreationDate(calBets.getTime());
		
		betDao.save(bet1);
		betDao.save(bet2);
		betDao.save(bet3);
		
		
	}

	private AdPicture createPicture(Ad ad, String filePath) {
		AdPicture picture = new AdPicture();
		picture.setFilePath(filePath);
		return picture;
	}

}