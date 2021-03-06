package gumga.framework.application;

import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class GumgaFinderTest {

    @Autowired
    private CompanyService service;

    @Test
    public void injectionSanityCheck() {
        assertNotNull(service);
    }

    @Test
    @Transactional
    public void criaEmpresaePesquisaViaQueryObject() {
        Company empresa = new Company();
        empresa.setName("Gumga");
        service.save(empresa);
        QueryObject query = new QueryObject();
        query.setQ("Gumga");
        query.setSearchFields("name");
        query.setSortField("name");
        List<Company> result = service.pesquisa(query).getValues();
        assert (!result.isEmpty() ); 
    }

    @Test
    @Transactional
    public void criaEmpresaePesquisaAvancadaViaQueryObject() {
        Company empresa = new Company();
        empresa.setName("Gumga");
        service.save(empresa);
        empresa = new Company();
        empresa.setName("Agmug");
        service.save(empresa);
        QueryObject query = new QueryObject();
        query.setAq("obj.name like '%'");
        SearchResult<Company> pesquisa = service.pesquisa(query);
        assertFalse(pesquisa.getValues().isEmpty());
    }

    @Test
    @Transactional
    public void criaEmpresaePesquisaAvancadaOrdenadaViaQueryObject() {
        Company empresa = new Company();
        empresa.setName("Gumga");
        service.save(empresa);
        empresa = new Company();
        empresa.setName("Agmug");
        service.save(empresa);
        QueryObject query = new QueryObject();
        query.setSortField("name");
        query.setAq("obj.name like '%'");
        SearchResult<Company> pesquisa = service.pesquisa(query);
        assertFalse(pesquisa.getValues().isEmpty());
    }

}
