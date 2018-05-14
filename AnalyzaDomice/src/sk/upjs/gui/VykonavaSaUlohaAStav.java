package sk.upjs.gui;

/**
 * Trieda, ktora sa zdiela vo viacerych vlaknach aby v nej nasli alebo zapisali
 * informaciu o tom ci sa akurat vykonava nejaka uloha a po skonceni vykonavania
 * sa tu zapise informacia o stave po skonceni vykonavania ulohy
 */
public class VykonavaSaUlohaAStav {

	private volatile boolean prebiehaVykonavanie;
	private volatile boolean vysledokVykonania;
	private volatile double objem;

	public boolean isPrebiehaVykonavanie() {
		return prebiehaVykonavanie;
	}

	public void setPrebiehaVykonavanie(boolean prebiehaVykonavanie) {
		this.prebiehaVykonavanie = prebiehaVykonavanie;
	}

	public boolean isVysledokVykonania() {
		return vysledokVykonania;
	}

	public void setVysledokVykonania(boolean vysledokVykonania) {
		this.vysledokVykonania = vysledokVykonania;
	}

	public double getObjem() {
		return objem;
	}

	public void setObjem(double objem) {
		this.objem = objem;
	}

}
