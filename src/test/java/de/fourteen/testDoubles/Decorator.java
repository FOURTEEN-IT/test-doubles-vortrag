package de.fourteen.testDoubles;

class Decorator implements SomeInterface {
  private final SomeInterface wrappee;

  Decorator(SomeInterface wrappee) {
    this.wrappee = wrappee;
  }

  @Override
  public Object doSomething() {
    // do something extra
    return wrappee.doSomething();
  }
}

interface SomeInterface {
  Object doSomething();
}

class SomeInterfaceStub implements SomeInterface {
  private Object stubbedDoSomething;

  @Override
  public Object doSomething() {
    return stubbedDoSomething;
  }
  
  void stubDoSomething(Object stubbedDoSomething) {
    this.stubbedDoSomething = stubbedDoSomething;
  }
}

class SomeInterfaceSpy implements SomeInterface {
  private final SomeInterface wrappee;
  private int timesDoSomethingHasBeenCalled = 0;

  SomeInterfaceSpy(SomeInterface wrappee) {
    this.wrappee = wrappee;
  }

  @Override
  public Object doSomething() {
    timesDoSomethingHasBeenCalled++;
    return wrappee.doSomething();
  }
  
  int timesDoSomethingHasBeenCalled() {
    return timesDoSomethingHasBeenCalled;
  }
}