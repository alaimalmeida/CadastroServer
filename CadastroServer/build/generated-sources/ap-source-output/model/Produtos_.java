package model;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Movimento;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-06-14T22:05:57", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Produtos.class)
public class Produtos_ { 

    public static volatile SingularAttribute<Produtos, BigDecimal> precoVenda;
    public static volatile SingularAttribute<Produtos, Integer> idProduto;
    public static volatile SingularAttribute<Produtos, String> nome;
    public static volatile CollectionAttribute<Produtos, Movimento> movimentoCollection;
    public static volatile SingularAttribute<Produtos, Integer> quantidade;

}