package br.ufal.ic.p2.jackut.utils;

import br.ufal.ic.p2.jackut.models.Comunidade;
import br.ufal.ic.p2.jackut.services.SystemService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileReadingService {

    public static void ReadFiles(SystemService systemService){
        Map<String, Comunidade> comunidades = new HashMap<>();


        readFile(systemService);

    }





    }
}
