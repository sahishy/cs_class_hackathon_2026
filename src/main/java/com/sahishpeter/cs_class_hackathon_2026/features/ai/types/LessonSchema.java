package com.sahishpeter.cs_class_hackathon_2026.features.ai.types;

import com.google.genai.types.Schema;
import com.google.genai.types.Type;

import java.util.List;
import java.util.Map;

public final class LessonSchema {

    public static final Schema LESSON_OUTPUT_SCHEMA;

    static {
        Schema lessonGraph = Schema.builder()
            .type(Type.Known.OBJECT)
            .description("A simple thumbnail graph represent the lesson.")
            .properties(Map.of(
                "expressions", Schema.builder()
                    .type(Type.Known.ARRAY)
                    .items(Schema.builder().type(Type.Known.STRING).build())
                    .description("Function expressions to render on the graph. These must be pure functions (e.g. x^2, sin(x), 2(x+3)). Nothing else should be added, like 'from 0 to 2' for example.")
                    .build(),

                "points", Schema.builder()
                    .type(Type.Known.ARRAY)
                    .items(
                        Schema.builder()
                            .type(Type.Known.ARRAY)
                            .items(Schema.builder().type(Type.Known.NUMBER).build())
                            .build()
                    )
                    .description("Coordinate points as [x, y] pairs.")
                    .build(),

                "shades", Schema.builder()
                    .type(Type.Known.ARRAY)
                    .items(
                        Schema.builder()
                            .type(Type.Known.OBJECT)
                            .properties(Map.of(
                                "leftEndpoint", Schema.builder()
                                    .type(Type.Known.NUMBER)
                                    .description("Left x-bound for shading.")
                                    .build(),
                                "rightEndpoint", Schema.builder()
                                    .type(Type.Known.NUMBER)
                                    .description("Right x-bound for shading.")
                                    .build(),
                                "expression", Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("Function expression, the shaded region will be below when y > 0 and above when y < 0.")
                                    .build()
                            ))
                            .required(List.of("leftEndpoint", "rightEndpoint", "expression"))
                            .build()
                    )
                    .description("Optional shaded regions on the graph.")
                    .build()
            ))
            .build();

        Schema lessonStep = Schema.builder()
            .type(Type.Known.OBJECT)
            .description("An individual lesson step.")
            .properties(Map.of(
                "title", Schema.builder()
                    .type(Type.Known.STRING)
                    .description("Title of this step.")
                    .build(),

                "content", Schema.builder()
                    .type(Type.Known.ARRAY)
                    .items(
                        Schema.builder()
                            .type(Type.Known.OBJECT)
                            .properties(Map.of(
                                "type", Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("Block type. Must be either 'text' or 'latex'.")
                                    .build(),
                                "value", Schema.builder()
                                    .type(Type.Known.STRING)
                                    .description("The text or LaTeX string content for this block. Latex should be eqnarray")
                                    .build()
                            ))
                            .required(List.of("type", "value"))
                            .build()
                    )
                    .description("Ordered content blocks for this step. Use type='text' for prose and type='latex' for equations.")
                    .build(),

                "graph", lessonGraph
            ))
            .required(List.of("title", "content", "graph"))
            .build();

        LESSON_OUTPUT_SCHEMA = Schema.builder()
            .type(Type.Known.OBJECT)
            .properties(Map.of(
                "title", Schema.builder()
                    .type(Type.Known.STRING)
                    .description("A unique, straightforward, and short title of the lesson.")
                    .build(),

                "topic", Schema.builder()
                    .type(Type.Known.STRING)
                    .description("The matching curriculum field category.")
                    .build(),

                "thumbnailGraph", lessonGraph,

                "steps", Schema.builder()
                    .type(Type.Known.ARRAY)
                    .items(lessonStep)
                    .description("Sequential instruction frames.")
                    .build()
            ))
            .required(List.of("title", "topic", "thumbnailGraph", "steps"))
            .build();
    }
}