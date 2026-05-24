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
                    .description("Function expressions to render on the graph.")
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

                "explanation", Schema.builder()
                    .type(Type.Known.STRING)
                    .description("Detailed educational text written in plain language only. Do not include raw math notation, equations, operators, or symbolic expressions here. Reference math only via [[latex:i]] placeholders, where i maps to latexSnippets[i].")
                    .build(),

                "latexSnippets", Schema.builder()
                    .type(Type.Known.ARRAY)
                    .items(Schema.builder().type(Type.Known.STRING).build())
                    .description("Array of valid LaTeX snippets referenced by [[latex:i]] markers in explanation. Use proper LaTeX commands like \\int, \\frac, \\sqrt, \\sum, braces for exponents/subscripts, and \\, where spacing is needed.")
                    .build(),

                "graph", lessonGraph
            ))
            .required(List.of("title", "explanation", "graph"))
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