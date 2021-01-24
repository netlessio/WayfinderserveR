
package org.jlucrum.realtime.analytics;

/**
 * Created by IntelliJ IDEA.
 * User: Evgeni Kappinen
 * Date: 4/15/11
 * Time: 8:49 PM
 */


public class ErrorEstimators {


    /**
     * MSE ( Mean Squared Error)
     * Uses minimum length of target or output
     *
     * @param target the value to be predicted of the time series
     * @param output model output (prediction)
     * @return error
     */
    public static double mse(double[] target, double[] output) {
        double sum = 0;
        int size;

        if (target.length < output.length) {
            size = target.length;
        } else {
            size = output.length;
        }

        for (int i = 0; i < size; i++) {
            sum += target[i] - output[i];
        }

        return sum / (double) (size - 1);
    }

    /**
     * MAPE (Mean absolute percentage error)
     * Uses minimum length of target or output
     * http://en.wikipedia.org/wiki/Mean_absolute_percentage_error
     *
     * @param target the value to be predicted of the time series
     * @param output model output (prediction)
     * @return error
     */
    public static double mape(double[] target, double[] output) {
        double sum = 0;
        double size;

        if (target.length < output.length) {
            size = target.length;
        } else {
            size = output.length;
        }

        for (int i = 0; i < size; i++) {
            sum += (target[i] - output[i]) / target[i];
        }

        return sum / (double) (size - 1);
    }

    /**
     * Theil Statistics
     *
     * @param target the value to be predicted of the time series