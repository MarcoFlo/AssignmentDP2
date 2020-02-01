package it.polito.dp2.BIB.sol3.service;


import it.polito.dp2.BIB.sol3.db.JournalsDB;
import it.polito.dp2.BIB.sol3.db.VolumesDB;
import it.polito.dp2.BIB.sol3.service.jaxb.Journal;
import it.polito.dp2.BIB.sol3.service.jaxb.Journals;
import it.polito.dp2.BIB.sol3.service.jaxb.Volume;
import it.polito.dp2.BIB.sol3.service.util.ResourseUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JournalsService {
    private final static int PAGE_SIZE = 2;

    private Map<String, Journal> mapJournals = JournalsDB.getMap();
    private Map<String, Volume> mapVolumes = VolumesDB.getMap();

    private ResourseUtils rutil;


    public JournalsService(UriInfo uriInfo) {
        rutil = new ResourseUtils((uriInfo.getBaseUriBuilder()));
    }

    public Journals getJournals(String keyword, int page) {
        Journals result = new Journals();

        List<Journal> jList = result.getJournal();
        List<Journal> allJList = mapJournals.values().stream().filter(journal -> journal.getTitle().contains(keyword)).collect(Collectors.toList());
        jList.addAll(allJList.stream().skip(page * PAGE_SIZE).limit(PAGE_SIZE).collect(Collectors.toList()));

        result.setTotalPages(BigInteger.valueOf((allJList.size() / PAGE_SIZE) + 1));
        result.setPage(BigInteger.valueOf(page));
        rutil.completeJournals(result, keyword, Math.min(page + 1, allJList.size() / PAGE_SIZE));

        return result;
    }

    public synchronized Response createUpdateJournal(Journal journal) {
        Response response;
        if (mapJournals.containsKey(journal.getISSN()))
            response = Response.status(204).build();
        else
            response = Response.ok().build();

        journal.getVolume().forEach(this::getVolumeFromSelf);
        rutil.completeJournal(journal);
        mapJournals.put(journal.getISSN(), journal);

        return response;
    }


    private Volume getVolumeFromSelf(String self) {
        Volume result = mapVolumes.get(self);
        if (result == null)
            throw new BadRequestException("The volume " + self + " doesn't exist", Response.status(Response.Status.BAD_REQUEST)
                    .entity("The volume " + self + " doesn't exist").build());

        return result;
    }
}
