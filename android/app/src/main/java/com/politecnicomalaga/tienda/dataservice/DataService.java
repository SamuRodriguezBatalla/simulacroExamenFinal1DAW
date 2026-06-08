package com.politecnicomalaga.tienda.dataservice;

import com.politecnicomalaga.tienda.model.Producto;

import java.util.List;

public interface DataService {
    public boolean addProducto(Producto p);
    public List<Producto> listAll();
    // y pesha más...
}
