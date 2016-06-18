/**
 * Musite
 * Copyright (C) 2010 Digital Biology Laboratory, University Of Missouri
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package musite.util;

/**
 *
 * @author Jianjiong Gao
 */
public final class StringConvertUtil {
    public interface ConverterFromString<C> {
        public C convert(String str);
    }

    public static ConverterFromString<String> getStringStringConverter() {
        return new ConverterFromString<String>() {
            public String convert(String str) {
                return str;
            }
        };
    }

    public static ConverterFromString<Integer> getIntegerStringConverter() {
        return new ConverterFromString<Integer>() {
        public Integer convert(String str) {
                return Integer.valueOf(str);
            }
        };
    }

    public static ConverterFromString<Double> getDoubleStringConverter() {
        return new ConverterFromString<Double>() {
            public Double convert(String str) {
                return Double.valueOf(str);
            }
        };
    }

    public static ConverterFromString<Boolean> getBooleanStringConverter() {
        return new ConverterFromString<Boolean>() {
            public Boolean convert(String str) {
                return Boolean.valueOf(str);
            }
        };
    }
}
