package sk.upjs.entity;

/**
 * Uchovava priebezne informacie o hodnotach elipsy, ktora by sa mala nacadzat
 * na vrchu useku casti stalaktitu
 */
public class Elipsa {

	private float minX;
	private float maxX;
	private float minY;
	private float maxY;
	private float maxZ;

	public Elipsa(float minX, float maxX, float minY, float maxY, float maxZ) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	/**
	 * Nastavy nove minimalne x len ak je mensie ako doterajsie nastavene
	 * 
	 * @param minX
	 *            suradnica, ktoru chceme skusit nastavit
	 * @return true ak sa zadana suradnica ulozila ako nove minimum
	 */
	public boolean setMinX(float minX) {
		if (this.minX > minX) {
			this.minX = minX;
			return true;
		}
		return false;
	}

	/**
	 * Nastavy nove maximalne x len ak je vacsie ako doterajsie nastavene
	 * 
	 * @param maxX
	 *            suradnica, ktoru chceme skusit nastavit
	 * @return true ak sa zadana suradnica ulozila ako nove maximum
	 */
	public boolean setMaxX(float maxX) {
		if (this.maxX < maxX) {
			this.maxX = maxX;
			return true;
		}
		return false;
	}

	/**
	 * Nastavy nove minimalne y len ak je mensie ako doterajsie nastavene
	 * 
	 * @param minY
	 *            suradnica, ktoru chceme skusit nastavit
	 * @return true ak sa zadana suradnica ulozila ako nove minimum
	 */
	public boolean setMinY(float minY) {
		if (this.minY > minY) {
			this.minY = minY;
			return true;
		}
		return false;
	}

	/**
	 * Nastavy nove maximalne y len ak je vacsie ako doterajsie nastavene
	 * 
	 * @param maxY
	 *            suradnica, ktoru chceme skusit nastavit
	 * @return true ak sa zadana suradnica ulozila ako nove maximum
	 */
	public boolean setMaxY(float maxY) {
		if (this.maxY < maxY) {
			this.maxY = maxY;
			return true;
		}
		return false;
	}

	/**
	 * Nastavy nove maximalne z len ak je vacsie ako doterajsie nastavene
	 * 
	 * @param maxZ
	 *            suradnica, ktoru chceme skusit nastavit
	 * @return true ak sa zadana suradnica ulozila ako nove maximum
	 */
	public boolean setMaxZ(float maxZ) {
		if (this.maxZ < maxZ) {
			this.maxZ = maxZ;
			return true;
		}
		return false;
	}

	public float getPoloosX() {
		return (maxX - minX) / 2;
	}

	public float getPoloosY() {
		return (maxY - minY) / 2;
	}

	public float getMaxZ() {
		return maxZ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(maxX);
		result = prime * result + Float.floatToIntBits(maxY);
		result = prime * result + Float.floatToIntBits(maxZ);
		result = prime * result + Float.floatToIntBits(minX);
		result = prime * result + Float.floatToIntBits(minY);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Elipsa other = (Elipsa) obj;
		if (Float.floatToIntBits(maxX) != Float.floatToIntBits(other.maxX))
			return false;
		if (Float.floatToIntBits(maxY) != Float.floatToIntBits(other.maxY))
			return false;
		if (Float.floatToIntBits(maxZ) != Float.floatToIntBits(other.maxZ))
			return false;
		if (Float.floatToIntBits(minX) != Float.floatToIntBits(other.minX))
			return false;
		if (Float.floatToIntBits(minY) != Float.floatToIntBits(other.minY))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Z: " + maxZ + " Pol: " + getPoloosX() + " " + getPoloosY() + " Sur: " + minX + " " + maxX + " " + minY
				+ " " + maxY;
	}
}
