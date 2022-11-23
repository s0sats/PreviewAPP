package com.namoadigital.prj001.core

import kotlinx.coroutines.flow.Flow

interface UseCases<in I, out R : Any?> {
    suspend operator fun invoke(input: I): Flow<IResult<R>>
}