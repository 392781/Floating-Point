/**
 * @author Ronald Lencevicius
 * CS 365
 */

public class fp {
    /**
     * adds two floating point values
     * only god knows what's happening in between
     * @param a integer to add
     * @param b integer to add
     * @return added integer
     */
    public static int add (int a, int b) {
        int[] lrgVal = fp.split(a);
        int[] smlVal = fp.split(b);
        int[] soln = new int[3];

        if (lrgVal[1] == 0xFF && lrgVal[2] == 0) {
            return combine(lrgVal);
        } else if (lrgVal[1] == 0xFF && lrgVal[2] != 0) {
            return combine(lrgVal);
        }

        if (smlVal[1] == 0xFF && smlVal[2] == 0) {
            return combine(smlVal);
        } else if (smlVal[1] == 0xFF && smlVal[2] != 0) {
            return combine(smlVal);
        }

        if (lrgVal[1] == 0 && lrgVal[2] == 0) {
            return combine(smlVal);
        }

        if (smlVal[1] == 0 && smlVal[2] == 0) {
            return combine(lrgVal);
        }

        lrgVal[2] += 0x800000;
        smlVal[2] += 0x800000;

        int exp = lrgVal[1] - smlVal[1];

        if (exp < 0) {
            lrgVal = fp.split(b);
            smlVal = fp.split(a);
            lrgVal[2] += 0x800000;
            smlVal[2] += 0x800000;
            exp = lrgVal[1] - smlVal[1];
        }

        smlVal[2] >>= exp;

        if (lrgVal[0] == 0 && smlVal[0] == 0) {
            soln[2] = lrgVal[2] + smlVal[2];
            soln[0] = 0;
        } else if ((lrgVal[0] == 1 && smlVal[0] == 0) || (lrgVal[0] == 0 && smlVal[0] == 1)) {
            soln[2] = lrgVal[2] - smlVal[2];
            if (lrgVal[0] == 1) {
                soln[0] = 1;
            } else {
                soln[0] = 0;
            }
        } else {
            soln[2] = lrgVal[2] + smlVal[2];
            soln[0] = 1;
        }

        soln[2] &= 0xFFFFFF;
        soln[1] = lrgVal[1];

        if (Integer.numberOfLeadingZeros(soln[2]) == 7) {
            soln[2] >>= 1;
            soln[1]++;
        } else {
            while (Integer.numberOfLeadingZeros(soln[2]) != 8) {
                soln[2] <<= 1;
                soln[1]--;
            }
        }

        return combine(soln);
    }

    /**
     * multiplies two floating point values
     * @param a integer to multiply
     * @param b integer to multiply
     * @return multiplied floating
     */
    public static int mul (int a, int b) {
        int[] lrgVal = split(a);
        int[] smlVal = split(b);
        int[] soln = new int[3];

        if (lrgVal[1] == 0xFF && lrgVal[2] == 0) {
            return combine(lrgVal);
        } else if (lrgVal[1] == 0xFF && lrgVal[2] != 0) {
            return combine(lrgVal);
        }

        if (smlVal[1] == 0xFF && smlVal[2] == 0) {
            return combine(smlVal);
        } else if (smlVal[1] == 0xFF && smlVal[2] != 0) {
            return combine(smlVal);
        }

        if (lrgVal[1] == 0 && lrgVal[2] == 0) {
            return combine(lrgVal);
        }

        if (smlVal[1] == 0 && smlVal[2] == 0) {
            return combine(smlVal);
        }

        lrgVal[2] += 0x800000;
        smlVal[2] += 0x800000;

        soln[0] = (lrgVal[0] ^ smlVal[0]);
        soln[1] = (lrgVal[1] + smlVal[1]) - 127;

        long mult = 0x0000000000000000;
        mult = ((long)lrgVal[2]) * smlVal[2];
        mult >>= 23;
        soln[2] = ((int) mult);

        soln[2] &= 0xFFFFFF;

        if (Integer.numberOfLeadingZeros(soln[2]) == 7) {
            soln[2] >>= 1;
            soln[1]++;
        } else {
            while (Integer.numberOfLeadingZeros(soln[2]) != 8) {
                soln[2] <<= 1;
                soln[1]--;
            }
        }

        return combine(soln);
    }

    /**
     * Splits an integer's bits into it's respective IEEE floating point bits
     * @param val integer to split
     * @return array with the sign, exponent, and fraction value in position 0, 1, and 2
     * respectively
     */
    private static int[] split (int val) {
        // moves sign bit 31 places, ands it with 1 for correct sign bit
        int sign = (val >> 31) & 0x1;
        // moves exponents 23 spaces, ands last 8 bits with 1 for correct exponent bits
        int expo = (val >> 23) & 0xFF;
        // no movement necessary since fraction is already the last 23 bits
        // ands the last 23 bits with 1 for correct fraction bits
        int frac = (val & 0x7FFFFF);

        int[] splitted = {sign, expo, frac};

        return splitted;
    }

    /**
     * Recombines the sign, exponent, fraction into binary integer representation
     * @param val integer array to combine
     * @return binary integer values
     */
    private static int combine (int[] val) {
        int number = 0x00000000;

        val[2] &= 0x7FFFFF;

        number += (val[2]);
        number += (val[1] << 23);
        number += (val[0] << 31);

        return number;
    }

    public static void main(String[] args) {

        int a = 0x00000000;
        int b = 0x07ffffff;

        System.out.println("Val a: " + Float.intBitsToFloat(a));
        System.out.println("Val b: " + Float.intBitsToFloat(b));

        System.out.println("\nadd: " + Float.intBitsToFloat(fp.add(b,a)));
        System.out.println("mul: " + Float.intBitsToFloat(fp.mul(b,a)));
    }
}