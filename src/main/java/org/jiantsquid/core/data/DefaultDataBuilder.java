package org.jiantsquid.core.data;

public class DefaultDataBuilder extends DataBuilder<Data>{

	@Override
	protected Data createData() {
		return new Data() ;
	}

}
