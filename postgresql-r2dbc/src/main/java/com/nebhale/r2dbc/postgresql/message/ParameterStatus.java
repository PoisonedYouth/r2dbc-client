/*
 * Copyright 2017-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nebhale.r2dbc.postgresql.message;

import io.netty.buffer.ByteBuf;

import java.util.Objects;

import static com.nebhale.r2dbc.postgresql.message.ByteBufUtils.readCStringUTF8;

public final class ParameterStatus implements BackendMessage {

    private final String name;

    private final String value;

    public ParameterStatus(String name, String value) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParameterStatus that = (ParameterStatus) o;
        return Objects.equals(this.name, that.name) &&
            Objects.equals(this.value, that.value);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }

    @Override
    public String toString() {
        return "ParameterStatus{" +
            "name='" + this.name + '\'' +
            ", value='" + this.value + '\'' +
            '}';
    }

    static ParameterStatus decode(ByteBuf in) {
        return new ParameterStatus(readCStringUTF8(in), readCStringUTF8(in));
    }

}
