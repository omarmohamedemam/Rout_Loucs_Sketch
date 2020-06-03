package sample;

import org.ejml.data.Complex64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.EigenDecomposition;

public class Utility {
    public static Complex64F[] findRoots(double... coefficients) {
        int N = coefficients.length-1;

        // Construct the companion matrix
        DenseMatrix64F c = new DenseMatrix64F(N,N);

        double a = coefficients[N];
        for( int i = 0; i < N; i++ ) {
            c.set(i,N-1,-coefficients[i]/a);
        }
        for( int i = 1; i < N; i++ ) {
            c.set(i,i-1,1);
        }


        EigenDecomposition<DenseMatrix64F> evd =  DecompositionFactory.eig(N,false);

        evd.decompose(c);

        Complex64F[] roots = new Complex64F[N];

        for( int i = 0; i < N; i++ ) {
            roots[i] = evd.getEigenvalue(i);
        }

        return roots;
    }
}
