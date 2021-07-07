package ru.vinigret.limit.urlico.uchetul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UchetULService {

    @Autowired
    private UchetULRepository uchrRepository;

    public void createMainGroupByGrbs(UchetUL grbs){
        if (uchrRepository.findByUchreditelEqualsAndLevelEqualsAndUchetULKind(grbs, 1, UchetULKind.SVOD).isEmpty()) {
            UchetUL newGroup = new UchetUL();
            newGroup.setPorNum(uchrRepository.lastPorNumGroupFromLevel(1));
            newGroup.setIerKod(pad(newGroup.getPorNum()));
            newGroup.setName(grbs.getName() + " (свод учреждений)");
            newGroup.setGrbs(false);
            newGroup.setGroupUchr(true);
            newGroup.setUchetULKind(UchetULKind.SVOD);
            newGroup.setLevel(1);
            uchrRepository.save(newGroup);
            grbs.setParent(newGroup);
        }
        createUchetULKindGroup(grbs);
    }

    public void createUchetULKindGroup(UchetUL uchetUL){
        if (uchrRepository.findByUchreditelEqualsAndLevelEqualsAndUchetULKind(uchetUL.getUchreditel(), 2, uchetUL.getUchetULKind()).isEmpty()) {
            UchetUL newGroup = new UchetUL();
            newGroup.setPorNum(uchetUL.getUchetULKind().ordinal());
            newGroup.setIerKod(uchetUL.getIerKod() + uchetUL.getUchetULKind().ordinal());
            newGroup.setName(uchetUL.getName() + " (свод " + UchetULKind.returnMap().get(uchetUL.getUchetULKind().ordinal()) + ")");
            //newGroup.setDateStart(uchetUL.getDateStart());
            //newGroup.setDateEnd(uchetUL.getDateEnd());
            newGroup.setGrbs(false);
            //newGroup.setUchreditel(uchetUL.getUchreditel());
            newGroup.setParent(uchetUL.getParent());
            newGroup.setGroupUchr(true);
            newGroup.setUchetULKind(uchetUL.getUchetULKind());
            newGroup.setLevel(2);
            uchrRepository.save(newGroup);
            uchetUL.setParent(newGroup);
        }
        uchrRepository.save(uchetUL);
    }

    public void createIerKod(UchetUL uchetUl){
        StringBuilder ierKod = new StringBuilder(5);
        ierKod.append(uchetUl.getRazdelBK());
        ierKod.append(uchetUl.getUchetULKind().ordinal());
    }

    private String pad(Integer porNum) {
        StringBuilder padded = new StringBuilder(3);
        padded.append(porNum);
        while (padded.length() < 3) {
            padded.insert(0, '0');
        }
        return padded.toString();
    }
}
