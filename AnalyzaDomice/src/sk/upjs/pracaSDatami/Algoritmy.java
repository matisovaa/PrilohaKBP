package sk.upjs.pracaSDatami;

import java.util.List;
import java.util.Set;

import sk.upjs.entity.Vertex;

public abstract interface Algoritmy {

	/**
	 * Naprogramovane podla clanku: Silvestre I., Rodrigues J.I., Figueiredo M. and
	 * Veiga-Pires C., 2014. High-resolution digital 3D models of Algar do Penico
	 * Chamber: limitations, challenges, and potential. International Journal of
	 * Speleology, 44 (1), 25-35. Tampa, FL (USA) ISSN 0392-6672
	 * http://dx.doi.org/10.5038/1827-806X.44.1.3
	 * 
	 * @param zohladnovatNormaly
	 *            true tak tato metoda funguje podla algoritmu z clanku; false tak
	 *            sa pri hladani lokalnych minim nezohladnuje podmienka z algoritmu
	 *            o tom, ze suradnica Z normaloveho vektora v lokalnom minime musi
	 *            byt zaporna (pouzitie pri 3D modely, ktory nema korektne normalove
	 *            vektory. Napriklad lebo sacnner, ktorym bolo scanovane mracno
	 *            bodov, z ktoreho je 3D model normaly nezaznamenaval)
	 * @return pole v ktorom je true na idx vrcholu z 3D modelu, v ktorom je lokalne
	 *         minimum
	 */
	public boolean[] lokalneMinimaCeleho(boolean zohladnovatNormaly);

	/**
	 * Vrati vrcholy 3D modelu, ktore predstavuju jeden stalaktit, ktory zacina v
	 * zadanom minime a konci vo vyske kde sa na tomto stalaktite najde prve rozvetvenie
	 * 
	 * @param idxMinimaStalaktitu
	 *            index vrcholu, ktory je minimom stalaktitu, ktory chceme vyseparovat z
	 *            3D modelu
	 * @return zoznam vrcholov, ktore tvoria stalaktit
	 */
	public List<Vertex> jedenStalaktitPoPrveRozvetvenie(int idxMinimaStalaktitu);

	/**
	 * Rozdeli vrcholy na dve mnoziny: true su oznacene vrcholy, ktore algoritmus
	 * oznacil za vrcholy stropu a false su oznacene vrcholy, o ktorych podla tohto
	 * algoritmu predpokladame ze patria stalaktitom kedze neboli oznavene za vrcholy
	 * stropu
	 * 
	 * @param uholMaxOdchylky
	 *            uhol, kolko je tolerovane, aby sa stena (trojuholnik) odchylovala
	 *            od vodorovnej roviny
	 * @param idxVrcholuZoStropu
	 *            idx vrcholu, z ktoreho zacne pri hladani stropu
	 * @return pole v ktorom true znamena, ze vrchol patri stropu
	 */
	public boolean[] oddelitStropAStalaktity(float uholMaxOdchylky, int idxVrcholuZoStropu);

	/**
	 * Vrcholy 3D modelu, ktore su oznacene, ze patria stropu by nemali byt minimom
	 * stalaktitu
	 * 
	 * @param lokalneMinimaCeleho
	 *            true je na indexoch vrcholov, ktore predstavuju lokalne minima
	 *            celeho 3D modelu
	 * @param stropAStalaktity
	 *            true je na indexoch vrcholov, ktore by mali predstavovat strop
	 * @return zoznam vrcholov, ktore su lokalnymi minimami 3D modelu, ale nie su
	 *         vrcholmi stropu
	 */
	public List<Vertex> minimaNepatriaceStropu(boolean[] lokalneMinimaCeleho, boolean[] stropAStalaktity);

	/**
	 * Ako stalaktity je oznaËen· Ëasù 3D modelu, ktor· zaËÌna v lok·lnych minim·ch,
	 * ktorÈ nepatria stropu a prid·vaj˙ sa tam vûdy susedia vybranÈho vrcholu,
	 * ktor˝ maj˙ v‰Ëöiu s˙radnicu Z ako vybran˝ vrchol ale dan˝ sused nepatrÌ
	 * stropu.
	 * 
	 * @param minimaNepatriaceStropu
	 *            vrcholy, v ktorych su lokalne minima 3D modelu, ktore nepatria
	 *            stropu
	 * @param stropAStalaktity
	 *            pole, kde true oznacuje, ze vrchol so danym indexom patri stropu
	 * @return pole, kde je false na indexoch vrcholov, ktore patria vyslednym
	 *         stalaktitom
	 */
	public boolean[] vyseparovanieStalaktitov(List<Vertex> minimaNepatriaceStropu, boolean[] stropAStalaktity);

	/**
	 * Najde komponenty suvislosti grafu (3D model) tvoreneho zadanymi vrcholmi
	 * (maju false)
	 * 
	 * @param vrcholyPreRozdelenie
	 *            false je na indexoch vrcholov, ktore chceme rozdelit
	 * @return mnozinu zoznamov, kde kazdy zoznam obsahuje vrcholy jednej suvisluej
	 *         casti 3D modelu (komponent suvislosti grafu)
	 */
	public Set<List<Vertex>> komoponentySuvislosti(boolean[] vrcholyPreRozdelenie);

	/**
	 * Vypocita objem stalaktitov, ktore su zlozene z vrcholov, ktore su zadane v
	 * stropAStalaktity hodnotou false. Najprv sa tieto stalaktity rozkuskuju na stalaktity bez
	 * rozvetveni, kde kazdy tento stalaktit zacina v jednom z lokalnych minim, ktore
	 * su zadane a obsahuje postupne vrcholy zadanych stalaktitov, ktore susedia s
	 * vrcholmi v uz najdenej casti stalaktitu a maju vacsiu suradnicu Z nez niektory
	 * vrchol z doteraz najdeneho useku stalaktitu s ktorym susedia.
	 * 
	 * Objem jedneho stalaktitu bez rozvetveni sa rata nasledovne: Pre kazdy usek
	 * stalaktitu, ktory ma vysku podla osi Z ako je zadana vyska useku prejde
	 * suradnicami vsetkych bodov, ktore danemu useku patria (ich suradnica Z ma
	 * hodnotu medzi Z suradnicami vysky useku) a zistime maximalnu a minimalnu
	 * suradnicu X a Y zo vsetkych tychto bodov. Z tychto zistenych hodnot
	 * maximalnej a minimalnej suradnice X a Y vyrobime elipsu, ktorej obsah by mal
	 * byt obsahom prierezu na vrchu useku. Vysledny objem je suctom objemov telies,
	 * ktore maju ako podstavy dve elipsy po sebe masledujucich usekov a vyska tohto
	 * telesa je vyska useku (pri najvrchnejsom telese je vyska rozdiel suradnice Z
	 * bodu s najvyssou suradnicou Z a suradnicou Z predchadzajucej elipsy)
	 * 
	 * @param stropAStalaktity
	 *            pole, kde false oznacuje, ze vrchol so danym indexom
	 *            predpokladame, ze patri stalaktitu
	 * @param minimaZadanychStalaktitov
	 *            minima, ktore su minimami stalaktitov, kde z kazdeho sa bude
	 *            vyhladavat jeden stalaktit bez rozvetveni
	 * @param vyskaUseku
	 *            rozsah suradnic Z vrcholov, ktore este budeme povazovat,ze patria
	 *            jednemu useku stalaktitu
	 * @return objem zadanych stalaktitov, ktory je suctom objemov jednotlivych stalaktitov
	 *         bez rozvetveni
	 */
	public double objemStalaktitov(boolean[] stropAStalaktity, List<Vertex> minimaZadanychStalaktitov, float vyskaUseku);

}
