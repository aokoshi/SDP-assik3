# Drawing with the Bridge Pattern

Topic A: Drawing. A Java 17 console application that separates shapes from rendering methods.

Repository: https://github.com/aokoshi/SDP-assik3

## Design

`Shape` stores the ID and a constructor-injected `Renderer`. `Circle` retains its radius;
`Square` retains its side. Their `execute()` methods delegate to the renderer interface.
`setImplementation(Renderer)` replaces the renderer while preserving the shape object and data.
Renderers return local text simulations; no external dependencies are required.

| Role | Class | Source |
| --- | --- | --- |
| Abstraction | Shape | [src/Shape.java](src/Shape.java) |
| A1 | Circle | [src/Circle.java](src/Circle.java) |
| A2 | Square | [src/Square.java](src/Square.java) |
| Implementor | Renderer | [src/Renderer.java](src/Renderer.java) |
| I1 | VectorRenderer | [src/VectorRenderer.java](src/VectorRenderer.java) |
| I2 | RasterRenderer | [src/RasterRenderer.java](src/RasterRenderer.java) |
| Client | Main | [src/Main.java](src/Main.java) |

The bridge field `private Renderer renderer`, constructor, `setImplementation(Renderer)`
and abstract `execute()` are in `Shape.java`. Delegating `execute()` implementations are
in `Circle.java` and `Square.java`. T5 is `Main.checkRuntimeSwitch()` and uses `original == current`.

## Build and run

Run from the project root with JDK 17 or later:

```sh
javac --release 17 -encoding UTF-8 -d out "@sources.txt"
java -cp out Main --demo
```

## Expected outcomes

| Check | Combination | Expected result |
| --- | --- | --- |
| T1 | Circle + VectorRenderer | `VECTOR circle radius=2` |
| T2 | Circle + RasterRenderer | `RASTER circle radius=2` |
| T3 | Square + VectorRenderer | `VECTOR square side=3` |
| T4 | Square + RasterRenderer | `RASTER square side=3` |
| T5 | Same Circle, VectorRenderer to RasterRenderer | `sameObject=true`, `stateUnchanged=true`; `VECTOR circle radius=2` then `RASTER circle radius=2`; ID `circle-switch` and radius `2` unchanged |

Each check compares real results with expected values. A failed check prints its expected
result. All checks run before the calculated summary. This initial version ends with
`SUMMARY: 5/5 PASS`. The process exits with code 0 on success, 1 on a failed check,
or 2 for invalid command-line arguments.
