import sympy


def read_input():
    f = open('../resources/input', 'r')
    for line in f.readlines():
        p, v = line.split(' @ ')
        p = list(map(int, p.split(', ')))
        v = list(map(int, v.split(', ')))
        yield p, v


if __name__ == "__main__":
    parsed = read_input()
    x = sympy.var('x')
    y = sympy.var('y')
    z = sympy.var('z')
    xv = sympy.var('xv')
    yv = sympy.var('yv')
    zv = sympy.var('zv')
    equations = []
    count = 0
    for point, velocity in parsed:
        count += 1
        t = sympy.var('t' + str(count))
        equations.append(sympy.Eq(x + t * xv, point[0] + velocity[0] * t))
        equations.append(sympy.Eq(y + t * yv, point[1] + velocity[1] * t))
        equations.append(sympy.Eq(z + t * zv, point[2] + velocity[2] * t))

        if count > 5:  # speedup hack, if we hit 5 hailstones, we are likely to hit them all
            break
    solution = sympy.solve(equations)[0]
    print(solution[x] + solution[y] + solution[z])
