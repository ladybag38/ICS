import java.util.Random;



 class SimpleDemoGA {

    Population population = new Population();
    Individual fittest;
    Individual secondFittest;
    int generationCount = 0;

    public static void main(String[] args) {

        Random rn = new Random();

        SimpleDemoGA demo = new SimpleDemoGA();

        //Задаём новую популяцию
        demo.population.initializePopulation(10);

        //Высчитываем оценку приспособленности для каждой особи
        demo.population.calculateFitness();

        System.out.println("Особь: " + demo.generationCount + " Оценка приспособленности: " + demo.population.fittest);

        //Особь с макс приспособленностью
        while (demo.population.fittest < 5) {
            ++demo.generationCount;

            //выборка
            demo.selection();

            //кроссовер
            demo.crossover();

            //Рандомные мутации
            if (rn.nextInt()%7 < 5) {
                demo.mutation();
            }

            //Добавляем наиболее живучее потомство в популяцию
            demo.addFittestOffspring();

            //Вычисляем новую оценку выживаемости
            demo.population.calculateFitness();

            System.out.println("Особь: " + demo.generationCount + "  Оценка приспособленности: " + demo.population.fittest);
        }

        System.out.println("\nНаиболее приспособлененая особь: " + demo.generationCount);
        System.out.println("Оценка приспособленности: "+demo.population.getFittest().fitness);
        System.out.print("Гены: ");
        for (int i = 0; i < 5; i++) {
            System.out.print(demo.population.getFittest().genes[i]);
        }

        System.out.println("");

    }

    //Selection
    void selection() {

        //Выбор наиболее прспособленной особи
        fittest = population.getFittest();

        //Выбор второй наиболее приспособленой особи
        secondFittest = population.getSecondFittest();
    }

    //Crossover
    void crossover() {
        Random rn = new Random();

        //Рандомно выбираем точку пересечения
        int crossOverPoint = rn.nextInt(population.individuals[0].geneLength);

        //Обмен генами медлу родителями
        for (int i = 0; i < crossOverPoint; i++) {
            int temp = fittest.genes[i];
            fittest.genes[i] = secondFittest.genes[i];
            secondFittest.genes[i] = temp;

        }

    }

    //Мутации
    void mutation() {
        Random rn = new Random();

        //Выбор рандомного гена для мутации
        int mutationPoint = rn.nextInt(population.individuals[0].geneLength);

        //Инвертировать значение гена
        if (fittest.genes[mutationPoint] == 0) {
            fittest.genes[mutationPoint] = 1;
        } else {
            fittest.genes[mutationPoint] = 0;
        }

        mutationPoint = rn.nextInt(population.individuals[0].geneLength);

        if (secondFittest.genes[mutationPoint] == 0) {
            secondFittest.genes[mutationPoint] = 1;
        } else {
            secondFittest.genes[mutationPoint] = 0;
        }
    }

    //Получение оценки выживаемости потомства
    Individual getFittestOffspring() {
        if (fittest.fitness > secondFittest.fitness) {
            return fittest;
        }
        return secondFittest;
    }


    //Заменим наименее приспособленной особ из наиболее приспособленного потомства
    void addFittestOffspring() {

        //Обновим оценки выживаемости потомства
        fittest.calcFitness();
        secondFittest.calcFitness();

        //получение индекса наименее приспособленной особи
        int leastFittestIndex = population.getLeastFittestIndex();
        //Замена особи из популяции с наименее низким индексом прспособленности на особь из нового поколения
        population.individuals[leastFittestIndex] = getFittestOffspring();
    }

}


//Генерация характеристик особи, ген и хромосом
class Individual {

    int fitness = 0;
    int[] genes = new int[5];
    int geneLength = 5;

    public Individual() {
        Random rn = new Random();

        //Рандомный выбор значений для генов особи (0/1)
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Math.abs(rn.nextInt() % 2);
        }

        fitness = 0;
    }

    //Вычисление оценки выживаемости
    public void calcFitness() {

        fitness = 0;
        for (int i = 0; i < 5; i++) {
            if (genes[i] == 1) {
                ++fitness;
            }
        }
    }

}

//Популяции
class Population {

    int popSize = 10;
    Individual[] individuals = new Individual[10];
    int fittest = 0;

    //Генерация популяции
    public void initializePopulation(int size) {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual();
        }
    }

    //получение оценки приспособленности первой особи
    public Individual getFittest() {
        int maxFit = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (maxFit <= individuals[i].fitness) {
                maxFit = individuals[i].fitness;
                maxFitIndex = i;
            }
        }
        fittest = individuals[maxFitIndex].fitness;
        return individuals[maxFitIndex];
    }

    //получение оценки приспособленности второй наиболее приспособленной особи
    public Individual getSecondFittest() {
        int maxFit1 = 0;
        int maxFit2 = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (individuals[i].fitness > individuals[maxFit1].fitness) {
                maxFit2 = maxFit1;
                maxFit1 = i;
            } else if (individuals[i].fitness > individuals[maxFit2].fitness) {
                maxFit2 = i;
            }
        }
        return individuals[maxFit2];
    }

    //получение оценки приспособленности наименее приспособленной особи
    public int getLeastFittestIndex() {
        int minFitVal = Integer.MAX_VALUE;
        int minFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (minFitVal >= individuals[i].fitness) {
                minFitVal = individuals[i].fitness;
                minFitIndex = i;
            }
        }
        return minFitIndex;
    }

    //Вычисление оценки прспособленности каждой особи
    public void calculateFitness() {

        for (int i = 0; i < individuals.length; i++) {
            individuals[i].calcFitness();
        }
        getFittest();
    }

}