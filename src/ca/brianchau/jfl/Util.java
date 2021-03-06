package ca.brianchau.jfl;

import java.util.Scanner;

/**
 * Created by brian on 2014-10-06.
 */
public class Util {

    public static boolean USE_CONS_FOR_LIST = false;

    public static Scanner s = new Scanner(System.in);

    // Function evaluation
    public static Lambda eval(Lambda f, Lambda ... args) {
        Lambda l = f;
        for (Lambda arg : args) {
            l = l.apply(arg);
        }
        return l;
    }

    // Static objects
    public static final Bool TRUE = new Bool(true);
    public static final Bool FALSE = new Bool(false);
    public static final Cons EMPTY = new Empty();

    // Constructors
    public static Str Str(String s) {
        return new Str(s);
    }
    public static Str Str(Lambda l) {
        return new Str(l.toString());
    }
    public static Int Int(int i) {
        return new Int(i);
    }
    public static Int Int(long l) {
        return new Int(l);
    }
    public static Flt Flt(double d) {
        return new Flt(d);
    }
    public static Bool Bool(boolean b) {
        return new Bool(b);
    }
    public static Cons Cons(Lambda l, Cons c) {
        return new Cons(l, c);
    }
    public static Cons List(Lambda ... ls) {
        Cons c = EMPTY;
        for (int i = ls.length - 1; i >= 0; --i) {
            c = Cons(ls[i], c);
        }
        return c;
    }
    public static Lambda Lambda(Lambda l) {
        return l;
    }

    // Identity operation
    public static Lambda identity = a -> a;

    // Boolean operations
    public static Lambda and = a -> b -> {
        if (a instanceof Bool && b instanceof Bool) {
            return Bool(((Bool) a).value() && ((Bool) b).value());
        }
        throw new JFLException("Unable to compare these values.");
    };
    public static Lambda or = a -> b -> {
        if (a instanceof Bool && b instanceof Bool) {
            return Bool(((Bool) a).value() || ((Bool) b).value());
        }
        throw new JFLException("Unable to compare these values.");
    };
    public static Lambda xor = a -> b -> {
        if (a instanceof Bool && b instanceof Bool) {
            return Bool(((Bool) a).value() ^ ((Bool) b).value());
        }
        throw new JFLException("Unable to compare these values.");
    };
    public static Lambda not = l -> {
        if (l instanceof Bool) {
            return l.equals(TRUE) ? FALSE : TRUE;
        }
        throw new JFLException("Unable to compare these values.");
    };

    // Comparison operations
    public static Lambda eq = a -> b -> a.equals(b) ? TRUE : FALSE;
    public static Lambda lt = a -> b -> {
        if (a instanceof Flt) {
            if (b instanceof Flt) {
                return Bool(((Flt) a).value() < ((Flt) b).value());
            } else if (b instanceof Int) {
                return Bool(((Flt) a).value() < ((Int) b).value());
            }
        } else if (a instanceof Int) {
            if (b instanceof Flt) {
                return Bool(((Int) a).value() < ((Flt) b).value());
            } else if (b instanceof Int) {
                return Bool(((Int) a).value() < ((Int) b).value());
            }
        }
        throw new JFLException("Unable to compare these values.");
    };
    public static Lambda gt = a -> b -> {
        if (a instanceof Flt) {
            if (b instanceof Flt) {
                return Bool(((Flt) a).value() > ((Flt) b).value());
            } else if (b instanceof Int) {
                return Bool(((Flt) a).value() > ((Int) b).value());
            }
        } else if (a instanceof Int) {
            if (b instanceof Flt) {
                return Bool(((Int) a).value() > ((Flt) b).value());
            } else if (b instanceof Int) {
                return Bool(((Int) a).value() > ((Int) b).value());
            }
        }
        throw new JFLException("Unable to compare these values.");
    };
    public static Lambda lte = a -> b -> not.apply(gt.apply(a).apply(b));
    public static Lambda gte = a -> b -> not.apply(lt.apply(a).apply(b));

    // Math operations
    public static Lambda isZero = l -> {
        if (l instanceof Int) {
            return Bool(((Int) l).value() == 0L);
        } else if (l instanceof Flt) {
            return Bool(((Flt) l).value() == 0.0);
        }
        return Bool(false);
    };
    public static Lambda add = a -> b -> {
        if (a instanceof Flt) {
            if (b instanceof Flt) {
                return Flt(((Flt) a).value() + ((Flt) b).value());
            } else if (b instanceof Int) {
                return Flt(((Flt) a).value() + ((Int) b).value());
            }
        } else if (a instanceof Int) {
            if (b instanceof Flt) {
                return Flt(((Int) a).value() + ((Flt) b).value());
            } else if (b instanceof Int) {
                return Int(((Int) a).value() + ((Int) b).value());
            }
        }
        throw new JFLException("Unable to add non-numerical values.");
    };
    public static Lambda sub = a -> b -> {
        if (a instanceof Flt) {
            if (b instanceof Flt) {
                return Flt(((Flt) a).value() - ((Flt) b).value());
            } else if (b instanceof Int) {
                return Flt(((Flt) a).value() - ((Int) b).value());
            }
        } else if (a instanceof Int) {
            if (b instanceof Flt) {
                return Flt(((Int) a).value() - ((Flt) b).value());
            } else if (b instanceof Int) {
                return Int(((Int) a).value() - ((Int) b).value());
            }
        }
        throw new JFLException("Unable to subtract non-numerical values.");
    };
    public static Lambda mul = a -> b -> {
        if (a instanceof Flt) {
            if (b instanceof Flt) {
                return Flt(((Flt) a).value() * ((Flt) b).value());
            } else if (b instanceof Int) {
                return Flt(((Flt) a).value() * ((Int) b).value());
            }
        } else if (a instanceof Int) {
            if (b instanceof Flt) {
                return Flt(((Int) a).value() * ((Flt) b).value());
            } else if (b instanceof Int) {
                return Int(((Int) a).value() * ((Int) b).value());
            }
        }
        throw new JFLException("Unable to multiply non-numerical values.");
    };
    public static Lambda div = a -> b -> {
        if (((Bool)isZero.apply(b)).value()) {
            throw new JFLException("/ by 0 not allowed.");
        } else if (a instanceof Flt) {
            if (b instanceof Flt) {
                return Flt(((Flt) a).value() / ((Flt) b).value());
            } else if (b instanceof Int) {
                return Flt(((Flt) a).value() / ((Int) b).value());
            }
        } else if (a instanceof Int) {
            if (b instanceof Flt) {
                return Flt(((Int) a).value() / ((Flt) b).value());
            } else if (b instanceof Int) {
                return Int(((Int) a).value() / ((Int) b).value());
            }
        }
        throw new JFLException("Unable to divide non-numerical values.");
    };
    public static Lambda mod = a -> b -> {
        if (((Bool)isZero.apply(b)).value()) {
            throw new JFLException("% by 0 not allowed.");
        } else if (a instanceof Flt) {
            if (b instanceof Flt) {
                return Flt(((Flt) a).value() % ((Flt) b).value());
            } else if (b instanceof Int) {
                return Flt(((Flt) a).value() % ((Int) b).value());
            }
        } else if (a instanceof Int) {
            if (b instanceof Flt) {
                return Flt(((Int) a).value() % ((Flt) b).value());
            } else if (b instanceof Int) {
                return Int(((Int) a).value() % ((Int) b).value());
            }
        }
        throw new JFLException("Unable to modulo non-numeric values.");
    };
    public static Lambda isOdd = l -> {
        if (l instanceof Int) {
            return eq.apply(Int(1)).apply(mod.apply(l).apply(Int(2)));
        }
        throw new JFLException("Unable to determine oddness of non-Int value.");
    };
    public static Lambda isEven = l -> {
        if (l instanceof Int) {
            return eq.apply(Int(0)).apply(mod.apply(l).apply(Int(2)));
        }
        throw new JFLException("Unable to determine oddness of non-Int value.");
    };
    public static Lambda add1 = add.apply(Int(1));
    public static Lambda sub1 = add.apply(Int(-1));

    // String operations
    public static Lambda concat = a -> b -> {
        return Str(a.toString() + b.toString());
    };

    // List operations
    public static Lambda cons = l -> c -> {
        if (c instanceof Cons) {
            return Cons(l, (Cons)c);
        }
        throw new JFLException("Unable to cons to a non-Cons.");
    };
    public static Bool isEmpty(Lambda l) {
        if (l instanceof Cons) {
            return Bool(((Cons) l).isEmpty());
        }
        throw new JFLException("Unable to evaluate isEmpty on a non-list.");
    }
    public static Lambda first(Lambda l) {
        if (l instanceof Cons) {
            return ((Cons) l).value();
        }
        throw new JFLException("Unable to evaluate first on a non-list.");
    }
    public static Lambda rest(Lambda l) {
        if (l instanceof Cons) {
            return ((Cons) l).next();
        }
        throw new JFLException("Unable to evaluate rest on a non-list.");
    }

    // Conditional
    public static boolean cond(Lambda l) {
        if (l instanceof Bool) {
            return ((Bool) l).value();
        }
        throw new JFLException("Unable to evaluate cond statement on a non-bool operation.");
    }

    // Recursion
    private static Lambda selfApply = f -> eval(f, f);
    public static Lambda Fix = f -> eval(selfApply, w -> eval(f, x -> eval(w, w, x)));

    // Higher order functions
    public static Lambda foldr = eval(Fix, foldr -> f -> b -> l -> cond(isEmpty(l)) ? b : eval(f, first(l), eval(foldr, f, b, rest(l))));
    public static Lambda foldl = eval(Fix, foldl -> f -> b -> l -> cond(isEmpty(l)) ? b : eval(foldl, f, eval(f, first(l), b), rest(l)));
    public static Lambda map = eval(Fix, map -> f -> l -> cond(isEmpty(l)) ? EMPTY : Cons(eval(f, first(l)), (Cons)eval(map, f, rest(l))));
    public static Lambda filter = eval(Fix, filter -> p -> l -> cond(isEmpty(l)) ? EMPTY : cond(eval(p, first(l))) ? Cons(first(l), (Cons)eval(filter, p, rest(l))) : eval(filter, p, rest(l)));
    public static Lambda buildList = eval(Fix, buildList -> c -> n -> f -> cond(eval(gte, c, n)) ? EMPTY : Cons(eval(f, c), (Cons)eval(buildList, eval(add1, c), n, f)), Int(0));

    // Input/Output
    public static Int getInt() {
        if (s.hasNextLong()) {
            return new Int(s.nextLong());
        }
        throw new JFLException("Unable to read Int from System.in.");
    }
    public static Flt getFlt() {
        if (s.hasNextDouble()) {
            return new Flt(s.nextDouble());
        }
        throw new JFLException("Unable to read Flt from System.in.");
    }
    public static Str getStr() {
        if (s.hasNext())  {
            return new Str(s.next());
        }
        throw new JFLException("Unable to read Str from System.in.");
    }
    public static Lambda println(Lambda l) {
        print(l);
        System.out.println();
        return l;
    }
    public static Lambda print(Lambda l) {
        if (!USE_CONS_FOR_LIST && l instanceof Cons) {
            System.out.print("(List");
            Cons c = (Cons)l;
            while (!isEmpty(c).value()) {
                System.out.print(" " + c.value().toString());
                c = c.next();
            }
            System.out.print(")");
        } else {
            System.out.print(l.toString());
        }
        return l;
    }
}
