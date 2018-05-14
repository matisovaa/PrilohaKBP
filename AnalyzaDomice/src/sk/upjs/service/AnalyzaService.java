package sk.upjs.service;

public interface AnalyzaService {

	/**
	 * @return Index vrcholu z nacitaneho 3D modelu, ktory ma najmensiu suradnicu Z
	 */
	public int idxGlobalnehoMin();

	/**
	 * @return Index vrcholu z nacitaneho 3D modelu, ktory ma najvacsiu suradnicu Z
	 */
	public int idxGlobalnehoMax();

	/**
	 * Vyrobi subor PLY s lokalnymi minimami celeho 3D modelu. Vyuziva algoritmus z
	 * clanku: Silvestre I., Rodrigues J.I., Figueiredo M. and Veiga-Pires C., 2014.
	 * High-resolution digital 3D models of Algar do Penico Chamber: limitations,
	 * challenges, and potential. International Journal of Speleology, 44 (1),
	 * 25-35. Tampa, FL (USA) ISSN 0392-6672
	 * http://dx.doi.org/10.5038/1827-806X.44.1.3
	 * 
	 * @param cielovyAdresar
	 *            do ktoreho sa maju ulozit novovytvorene subory
	 * @param nazovSuboru
	 *            nazov noveho suboru do ktoreho sa ulozia vrcholy
	 * @param zohladnovatNormaly
	 *            true tak tato metoda funguje podla algoritmu z clanku; false tak
	 *            sa pri hladani lokalnych minim nezohladnuje podmienka z algoritmu
	 *            o tom, ze suradnica Z normaloveho vektora v lokalnom minime musi
	 *            byt zaporna (pouzitie pri 3D modely, ktory nema korektne normalove
	 *            vektory. Napriklad lebo sacnner, ktorym bolo scanovane mracno
	 *            bodov, z ktoreho je 3D model normaly nezaznamenaval)
	 * @return true ak sa podarilo vyrobit subor
	 */
	public boolean vyrobSuborSLokalnymiMinimamiCeleho3Dmodelu(String cielovyAdresar, String nazovSuboru,
			boolean zohladnovatNormaly);

	/**
	 * Vyhlada v nacitanom 3D modely vsetky stalaktity po prve rozvetvenie, ktore maju
	 * minimum v nejakom lokalnom minime 3D modelu a ulozi ich do suboru/suborov
	 * podla toho co je zadane v parametroch metody
	 * 
	 * @param cielovyAdresar
	 *            do ktoreho sa maju ulozit novovytvorene subory
	 * @param nazovSuboru
	 *            nazov suboru do ktoreho sa ulozia vrcholy
	 * @param zohladnovatNormaly
	 *            true tak tato metoda funguje podla algoritmu z clanku; false tak
	 *            sa pri hladani lokalnych minim nezohladnuje podmienka z algoritmu
	 *            o tom, ze suradnica Z normaloveho vektora v lokalnom minime musi
	 *            byt zaporna (pouzitie pri 3D modely, ktory nema korektne normalove
	 *            vektory. Napriklad lebo sacnner, ktorym bolo scanovane mracno
	 *            bodov, z ktoreho je 3D model normaly nezaznamenaval)
	 * @param doJednehoSuboru
	 *            true, ak chceme stalaktity zapisat spolu do jedneho suboru (steny
	 *            medzi vrcholmi z dvoch roznych stalaktitov sa nezapisu); false, ak
	 *            chceme vyrobit pre kazdy stalaktit osobitny subor
	 * @param ajOsamoteneBody
	 *            true zapise aj stalaktity tvorene len jednym vrcholom (osamoteny bod v
	 *            priestore)
	 * @return true ak sa podarilo vyrobit subor
	 */
	public boolean vsetkyStalaktityPoPrveRozvetvenieDoSuboru(String cielovyAdresar, String nazovSuboru,
			boolean zohladnovatNormaly, boolean doJednehoSuboru, boolean ajOsamoteneBody);

	/**
	 * Vyhlada v nacitanom 3D modely vsetky stalaktity po prve rozvetvenie, ktore maju
	 * minimum v nejakom lokalnom minime 3D modelu, ktore nepatria stropu a ulozi
	 * ich do suboru/suborov podla toho co je zadane v parametroch metody
	 * 
	 * @param cielovyAdresar
	 *            do ktoreho sa maju ulozit novovytvorene subory
	 * @param nazovSuboru
	 *            nazov suboru do ktoreho sa ulozia vrcholy
	 * @param zohladnovatNormaly
	 *            true tak tato metoda funguje podla algoritmu z clanku; false tak
	 *            sa pri hladani lokalnych minim nezohladnuje podmienka z algoritmu
	 *            o tom, ze suradnica Z normaloveho vektora v lokalnom minime musi
	 *            byt zaporna (pouzitie pri 3D modely, ktory nema korektne normalove
	 *            vektory. Napriklad lebo sacnner, ktorym bolo scanovane mracno
	 *            bodov, z ktoreho je 3D model normaly nezaznamenaval)
	 * @param doJednehoSuboru
	 *            true, ak chceme stalaktity zapisat spolu do jedneho suboru (steny
	 *            medzi vrcholmi z dvoch roznych stalaktitov sa nezapisu); false, ak
	 *            chceme vyrobit pre kazdy stalaktit osobitny subor
	 * @param ajOsamoteneBody
	 *            true zapise aj stalaktity tvorene len jednym vrcholom (osamoteny bod v
	 *            priestore)
	 * @param uholMaxOdchylky
	 *            uhol, kolko je tolerovane, aby sa steny (trojuholniky) na strope
	 *            odchylovali od vodorovnej roviny
	 * @param idxVrcholuZoStropu
	 *            idx vrcholu, o ktorom predpokladame, ze patri strou (napriklad
	 *            globalne maximum 3D modelu)
	 * @return true ak sa podarilo vyrobit subor
	 */
	public boolean vsetkyStalaktityPoPrveRozvetvenieDoSuboru(String cielovyAdresar, String nazovSuboru,
			boolean zohladnovatNormaly, boolean doJednehoSuboru, boolean ajOsamoteneBody, float uholMaxOdchylky,
			int idxVrcholuZoStropu);

	/**
	 * Vyrobi subor s stalaktitmi co ostali po oddeleni stropu podla zadanych
	 * parametrov. Ak je parameter ajSuborSOddelenymStropom nastaveny na true tak
	 * vyrobi aj subor so stropom, ktory bol oddeleny od tychto stalaktitov.
	 * 
	 * @param cielovyAdresar
	 *            do ktoreho sa maju ulozit novovytvorene subory
	 * @param nazovSuboru
	 *            nazov suboru do ktoreho sa ulozia vrcholy
	 * @param uholMaxOdchylky
	 *            uhol, kolko je tolerovane, aby sa steny (trojuholniky) na strope
	 *            odchylovali od vodorovnej roviny
	 * @param idxVrcholuZoStropu
	 *            idx vrcholu, o ktorom predpokladame, ze patri strou (napriklad
	 *            globalne maximum 3D modelu)
	 * @param ajSuborSOddelenymStropom
	 *            true tak vyrobi aj subor so stropom, ktory bol oddeleny od stalaktitov
	 *            (vyrobi dva subory); false tak vyrobi len subor s stalaktitmi
	 * @param doJednehoSuboru
	 *            true, ak chceme stalaktity zapisat spolu do jedneho suboru (steny
	 *            medzi vrcholmi z dvoch roznych stalaktitov sa nezapisu); false, ak
	 *            chceme vyrobit pre kazdy stalaktit osobitny subor
	 * @param ajOsamoteneBody
	 *            true zapise aj stalaktity tvorene len jednym vrcholom (osamoteny bod v
	 *            priestore)
	 * @return true ak sa podarilo vyrobit subor
	 */
	public boolean vyrobSuborSoStalaktitmiPoOddeleniStropu(String cielovyAdresar, String nazovSuboru, float uholMaxOdchylky,
			int idxVrcholuZoStropu, boolean ajSuborSOddelenymStropom, boolean doJednehoSuboru, boolean ajOsamoteneBody);

	/**
	 * Vyrobi subor s lokalny minimami 3D modelu, ktore nepatria stropu. Vrcholy 3D
	 * modelu, ktore su oznacene, ze patria stropu by nemali byt minimom stalaktitu
	 * 
	 * @param cielovyAdresar
	 *            do ktoreho sa maju ulozit novovytvorene subory
	 * @param nazovSuboru
	 *            nazov suboru do ktoreho sa ulozia vrcholy
	 * @param uholMaxOdchylky
	 *            uhol, kolko je tolerovane, aby sa steny (trojuholniky) na strope
	 *            odchylovali od vodorovnej roviny
	 * @param idxVrcholuZoStropu
	 *            idx vrcholu, o ktorom predpokladame, ze patri strou (napriklad
	 *            globalne maximum 3D modelu)
	 * @param zohladnovatNormaly
	 *            true tak tato metoda funguje podla algoritmu z clanku; false tak
	 *            sa pri hladani lokalnych minim nezohladnuje podmienka z algoritmu
	 *            o tom, ze suradnica Z normaloveho vektora v lokalnom minime musi
	 *            byt zaporna (pouzitie pri 3D modely, ktory nema korektne normalove
	 *            vektory. Napriklad lebo sacnner, ktorym bolo scanovane mracno
	 *            bodov, z ktoreho je 3D model normaly nezaznamenaval)
	 * @return true ak sa podarilo vyrobit subor
	 */
	public boolean vyrobSuborSMinimamiKtoreNepatriaStropu(String cielovyAdresar, String nazovSuboru,
			float uholMaxOdchylky, int idxVrcholuZoStropu, boolean zohladnovatNormaly);

	/**
	 * Vyroby osobitny subor ply pre kazdy komponent suvislosti nacitaneho 3D modelu
	 * 
	 * @param cielovyAdresar
	 *            do ktoreho sa maju ulozit novovytvorene subory
	 * @param nazovSuboru
	 *            nazov suboru do ktoreho sa ulozia vrcholy
	 * @param ajOsamoteneBody
	 *            false je odporucane, lebo nevyrobi subori, v ktorych je zapisany
	 *            len jeden vrchol; true vyrobi subor aj pre vsetky komponenty
	 *            suvislosti tvorene len jednym vrcholom
	 * @return true ak sa podarilo vyrobit subor
	 */
	public boolean vyrobSuborySKomponentmiSuvislosti(String cielovyAdresar, String nazovSuboru,
			boolean ajOsamoteneBody);

	/**
	 * Vyrobi subor s vyseparovanimi stalaktitmi co ostali po oddeleni stropu podla zadanych
	 * parametrov. Ak je parameter ajSuborSOddelenymStropom nastaveny na true tak
	 * vyrobi aj subor so stropom, ktory bol oddeleny od tychto stalaktitov.
	 * 
	 * @param cielovyAdresar
	 *            do ktoreho sa maju ulozit novovytvorene subory
	 * @param nazovSuboru
	 *            nazov suboru do ktoreho sa ulozia vrcholy
	 * @param uholMaxOdchylky
	 *            uhol, kolko je tolerovane, aby sa steny (trojuholniky) na strope
	 *            odchylovali od vodorovnej roviny
	 * @param idxVrcholuZoStropu
	 *            idx vrcholu, o ktorom predpokladame, ze patri strou (napriklad
	 *            globalne maximum 3D modelu)
	 * @param zohladnovatNormaly
	 *            true tak tato metoda hlada lokalne minima 3D modelu podla
	 *            algoritmu z clanku; false tak sa pri hladani lokalnych minim
	 *            nezohladnuje podmienka z algoritmu o tom, ze suradnica Z
	 *            normaloveho vektora v lokalnom minime musi byt zaporna (pouzitie
	 *            pri 3D modely, ktory nema korektne normalove vektory. Napriklad
	 *            lebo sacnner, ktorym bolo scanovane mracno bodov, z ktoreho je 3D
	 *            model normaly nezaznamenaval)
	 * @param ajSuborSOddelenymStropom
	 *            true tak vyrobi aj subor so stropom, ktory bol oddeleny od stalaktitov
	 *            (vyrobi dva subory); false tak vyrobi len subor s stalaktitmi
	 * @param doJednehoSuboru
	 *            true, ak chceme stalaktity zapisat spolu do jedneho suboru (steny
	 *            medzi vrcholmi z dvoch roznych stalaktitov sa nezapisu); false, ak
	 *            chceme vyrobit pre kazdy stalaktit osobitny subor
	 * @param ajOsamoteneBody
	 *            true zapise aj stalaktity tvorene len jednym vrcholom (osamoteny bod v
	 *            priestore)
	 * @return true ak sa podarilo vyrobit subor
	 */
	public boolean vyrobSuborSVyseparovanimiStalaktitmi(String cielovyAdresar, String nazovSuboru, float uholMaxOdchylky,
			int idxVrcholuZoStropu, boolean zohladnovatNormaly, boolean ajSuborSOddelenymStropom,
			boolean doJednehoSuboru, boolean ajOsamoteneBody);

	/**
	 * Najprv podla zadanych parametrov najde cast 3D modelu, ktora by mala patrit
	 * vyseparovanym stalaktitom zadaneho 3D modelu. Spusti algoritmus na vyratanie objemu
	 * stalaktitov (presnejsi popis fungovania tohto algoritmu je popisany v triede
	 * Algoritmy).
	 * 
	 * @param uholMaxOdchylky
	 *            uhol, kolko je tolerovane, aby sa steny (trojuholniky) na strope
	 *            odchylovali od vodorovnej roviny
	 * @param idxVrcholuZoStropu
	 *            idx vrcholu, o ktorom predpokladame, ze patri strou (napriklad
	 *            globalne maximum 3D modelu)
	 * @param vyskaUseku
	 *            rozsah suradnic Z vrcholov, ktore este budeme povazovat,ze patria
	 *            jednemu useku stalaktitu
	 * @param zohladnovatNormaly
	 *            true tak tato metoda hlada lokalne minima 3D modelu podla
	 *            algoritmu z clanku; false tak sa pri hladani lokalnych minim
	 *            nezohladnuje podmienka z algoritmu o tom, ze suradnica Z
	 *            normaloveho vektora v lokalnom minime musi byt zaporna (pouzitie
	 *            pri 3D modely, ktory nema korektne normalove vektory. Napriklad
	 *            lebo sacnner, ktorym bolo scanovane mracno bodov, z ktoreho je 3D
	 *            model normaly nezaznamenaval)
	 * @return objem vyseparovanych stalaktitov 3D modelu
	 */
	public double objemVyseparovanychStalaktitovZCeleho3DModelu(float uholMaxOdchylky, int idxVrcholuZoStropu, float vyskaUseku,
			boolean zohladnovatNormaly);

	/**
	 * Predpoklada, ze v nacitanom subore sa nachadzaju len stalaktity bez stropu.
	 * Vypocita objem tychto stalaktitov pomocou algoritmu z triedy Algoritmy.
	 * 
	 * @param vyskaUseku
	 *            rozsah suradnic Z vrcholov, ktore este budeme povazovat,ze patria
	 *            jednemu useku stalaktitu
	 * @param zohladnovatNormaly
	 *            true tak tato metoda hlada lokalne minima 3D modelu podla
	 *            algoritmu z clanku; false tak sa pri hladani lokalnych minim
	 *            nezohladnuje podmienka z algoritmu o tom, ze suradnica Z
	 *            normaloveho vektora v lokalnom minime musi byt zaporna (pouzitie
	 *            pri 3D modely, ktory nema korektne normalove vektory. Napriklad
	 *            lebo sacnner, ktorym bolo scanovane mracno bodov, z ktoreho je 3D
	 *            model normaly nezaznamenaval)
	 * @return objem stalaktitov 3D modelu v ktorom su len stalaktity
	 */
	public double objemStalaktitovZoSuboruLenSoStalaktitmi(float vyskaUseku, boolean zohladnovatNormaly);

}
