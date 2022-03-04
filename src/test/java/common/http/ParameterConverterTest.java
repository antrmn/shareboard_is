package common.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParameterConverterTest {

    //metodo di utilità che mi consente di "ignorare" la parte relativa a request.getParameter
    private <R> R convert(String value, BiFunction<ParameterConverter,String,R> converterMethod){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("iWillReturnValue")).thenReturn(value);

        ParameterConverter parameterConverter = new ParameterConverter(request);
        return converterMethod.apply(parameterConverter, "iWillReturnValue");
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,-5,Integer.MAX_VALUE, Integer.MIN_VALUE})
    void testIntParameter(int n){
        String str = String.valueOf(n);
        OptionalInt optionalInt = convert(str, ParameterConverter::getIntParameter);
        assertFalse(optionalInt.isEmpty());
        assertEquals(n, optionalInt.getAsInt());
    }

    @Test
    void testIntParameterOverflow(){
        long max = Integer.MAX_VALUE;
        max++;
        OptionalInt optionalInt = convert(String.valueOf(max), ParameterConverter::getIntParameter);
        assertTrue(optionalInt.isEmpty());

        long min = Integer.MIN_VALUE;
        min--;
        OptionalInt optionalIntMin = convert(String.valueOf(min), ParameterConverter::getIntParameter);
        assertTrue(optionalIntMin.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs={Long.MAX_VALUE, Long.MIN_VALUE, 0, -1, 50})
    void testLongParameter(long n){
        String str = String.valueOf(n);
        OptionalLong optionalLong = convert(str, ParameterConverter::getLongParameter);
        assertFalse(optionalLong.isEmpty());
        assertEquals(n, optionalLong.getAsLong());
    }

    @Test
    void testLongParameterOverflow(){
        BigInteger maxPlusOne = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);
        OptionalLong optionalLong = convert(maxPlusOne.toString(), ParameterConverter::getLongParameter);
        assertTrue(optionalLong.isEmpty());

        BigInteger minMinusOne = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE);
        OptionalLong optionalLong2 = convert(minMinusOne.toString(), ParameterConverter::getLongParameter);
        assertTrue(optionalLong2.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, Double.MAX_VALUE, Double.MIN_VALUE, -1, 0.234, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
    void testDoubleParameter(double n){
        String str = String.valueOf(n);
        OptionalDouble optionalDouble = convert(str,ParameterConverter::getDoubleParameter);
        assertFalse(optionalDouble.isEmpty());
    }

    @Test
    void testDoubleOverflow(){
        @SuppressWarnings("NumericOverflow")
        double max = Double.MAX_VALUE * 2;
        OptionalDouble optionalDouble = convert(String.valueOf(max),ParameterConverter::getDoubleParameter);
        assertFalse(optionalDouble.isEmpty());
        assertEquals(Double.POSITIVE_INFINITY, optionalDouble.getAsDouble());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2021-10-04", "1938-02-02"})
    void testDateParameter(String date){
        Optional<LocalDate> optionalLocalDate = convert(date, ParameterConverter::getDateParameter);
        assertFalse(optionalLocalDate.isEmpty());
        assertEquals(LocalDate.parse(date),optionalLocalDate.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {"g40", "x", "!", "."})
    @NullAndEmptySource
    void testInvalidParameters(String str){
        assertTrue(convert(str,ParameterConverter::getIntParameter).isEmpty());
        assertTrue(convert(str,ParameterConverter::getDoubleParameter).isEmpty());
        assertTrue(convert(str,ParameterConverter::getLongParameter).isEmpty());
        assertTrue(convert(str, ParameterConverter::getDateParameter).isEmpty());
    }
}