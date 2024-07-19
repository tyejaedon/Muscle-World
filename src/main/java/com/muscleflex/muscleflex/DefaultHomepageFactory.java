package com.muscleflex.muscleflex;

public class DefaultHomepageFactory implements HomepageFactory {
    @Override
    public Homepage createHomepage(int width, int height) {
        return new Homepage(width, height);
    }
}

