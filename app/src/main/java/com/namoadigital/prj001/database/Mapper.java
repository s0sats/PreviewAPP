package com.namoadigital.prj001.database;

/**
 * Created by neomatrix on 09/01/17.
 */

public interface Mapper <From, To> {
    To map(From from);
}
